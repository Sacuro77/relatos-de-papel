# Fase 5 - comms-service y notificacion por email

Proyecto: Relatos de Papel  
Modulo creado: `comms-service`

## Objetivo

Crear `comms-service` para consumir eventos `OrderCreated` desde RabbitMQ y gestionar la notificacion por email.

En esta fase no se implementan WebSocket, Gemini ni consumidor adicional. El servicio queda preparado para integrarlos en fases posteriores.

## Servicio creado

| Propiedad | Valor |
|---|---|
| Nombre | `comms-service` |
| Puerto | `8084` |
| Eureka | Se registra como `comms-service` |
| RabbitMQ host local | `localhost:5672` |
| Modo email local | `MAIL_ENABLED=false` |

## Configuracion RabbitMQ

El servicio consume la misma cola publicada por `orders-service`:

| Elemento | Nombre |
|---|---|
| Exchange | `relatos.orders.exchange` |
| Routing key | `orders.created` |
| Queue | `relatos.orders.created.queue` |

Propiedades:

```properties
spring.rabbitmq.host=${RABBITMQ_HOST:localhost}
spring.rabbitmq.port=${RABBITMQ_PORT:5672}
spring.rabbitmq.username=${RABBITMQ_USERNAME:guest}
spring.rabbitmq.password=${RABBITMQ_PASSWORD:guest}

app.rabbitmq.orders.exchange=${ORDERS_EXCHANGE:relatos.orders.exchange}
app.rabbitmq.orders.created-routing-key=${ORDERS_CREATED_ROUTING_KEY:orders.created}
app.rabbitmq.orders.created-queue=${ORDERS_CREATED_QUEUE:relatos.orders.created.queue}
```

## Evento consumido

`OrderCreatedListener` escucha:

```text
relatos.orders.created.queue
```

El mensaje se parsea a `OrderCreatedEvent` con:

- `eventId`;
- `eventType`;
- `orderId`;
- `userId`;
- `userEmail`;
- `status`;
- `total`;
- `items`;
- `createdAt`.

Log esperado al consumir:

```text
OrderCreated recibido orderId=... userId=... email=... total=...
```

## Email y modo mock

Propiedades previstas:

```properties
MAIL_HOST
MAIL_PORT
MAIL_USERNAME
MAIL_PASSWORD
MAIL_FROM
MAIL_ENABLED
```

Por defecto:

```properties
MAIL_ENABLED=false
```

Con `MAIL_ENABLED=false`, el servicio no intenta conectar a SMTP y registra el correo simulado en logs:

```text
MAIL MOCK to=... from=... subject="Pedido confirmado #..." body="..."
```

El contenido incluye:

- numero de pedido;
- usuario;
- estado;
- total;
- lista de items.

## Nota sobre SMTP real y Spring Mail

Durante la implementacion se intento agregar `spring-boot-starter-mail`, pero Maven no pudo descargar el artefacto por un error de certificado PKIX contra Maven Central.

Para mantener la fase compilable y demostrable, se implemento:

- modo mock/log por defecto con `MAIL_ENABLED=false`;
- envio SMTP real basico con Java estandar cuando `MAIL_ENABLED=true`;
- soporte de `MAIL_SMTP_AUTH` y `MAIL_SMTP_STARTTLS`.

Pendiente tecnico para cumplir estrictamente "Spring Mail":

1. Resolver acceso Maven/certificados.
2. Agregar `spring-boot-starter-mail`.
3. Inyectar `JavaMailSender`.
4. Sustituir el cliente SMTP estandar por Spring Mail.

## Clases creadas

| Clase | Responsabilidad |
|---|---|
| `CommsServiceApplication` | Arranque del microservicio. |
| `RabbitMqConfig` | Declara exchange, queue, binding y converter JSON. |
| `MailProperties` | Configura `MAIL_ENABLED` y `MAIL_FROM`. |
| `OrderCreatedEvent` | DTO del evento recibido. |
| `OrderCreatedItemEvent` | DTO de items del evento. |
| `OrderCreatedListener` | Consume mensajes desde RabbitMQ. |
| `EmailNotificationService` | Genera contenido de email, decide mock o SMTP real. |
| `SmtpEmailClient` | Cliente SMTP basico con Java estandar para `MAIL_ENABLED=true`. |

## Comandos PowerShell para probar

Compilar:

```powershell
Push-Location comms-service
.\mvnw.cmd compile
Pop-Location
```

Levantar RabbitMQ si no esta activo:

```powershell
docker run --name rabbitmq-relatos -p 5672:5672 -p 15672:15672 -d rabbitmq:3-management
```

Levantar `comms-service`:

```powershell
Push-Location comms-service
.\mvnw.cmd spring-boot:run
Pop-Location
```

Crear pedido por Gateway:

```powershell
$login = curl.exe -s -X POST http://localhost:8080/api/v1/auth/login `
  -H "Content-Type: application/json" `
  -d "{\"email\":\"cliente@relatos.com\",\"password\":\"123456\"}" | ConvertFrom-Json

$opaque = $login.opaqueToken

curl.exe -i -X POST http://localhost:8080/api/v1/orders `
  -H "Authorization: Bearer $opaque" `
  -H "Content-Type: application/json" `
  -d "{\"items\":[{\"bookId\":1,\"quantity\":1}]}"
```

Evidencias esperadas:

- `orders-service`: `OrderCreated publicado orderId=... userId=... routingKey=orders.created`.
- `comms-service`: `OrderCreated recibido orderId=... userId=... email=... total=...`.
- `comms-service`: `MAIL MOCK to=... subject="Pedido confirmado #..."`.
- RabbitMQ Management muestra que la cola baja mensajes al consumir.

## Validacion realizada

Se ejecuto:

```powershell
Push-Location comms-service
.\mvnw.cmd compile
Pop-Location
```

Resultado: `BUILD SUCCESS`.
