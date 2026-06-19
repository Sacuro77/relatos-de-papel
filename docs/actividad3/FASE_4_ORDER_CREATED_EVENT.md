# Fase 4 - Evento OrderCreated

Proyecto: Relatos de Papel  
Modulo modificado: `orders-service`

## Objetivo

Publicar un evento `OrderCreated` en RabbitMQ cada vez que `orders-service` registre correctamente una compra.

La publicacion ocurre despues de persistir el pedido. Si RabbitMQ falla, la creacion del pedido no se revierte: se registra un error en logs y queda documentado como riesgo de consistencia eventual.

## Configuracion RabbitMQ

| Elemento | Nombre |
|---|---|
| Exchange | `relatos.orders.exchange` |
| Tipo exchange | `direct` |
| Routing key | `orders.created` |
| Queue | `relatos.orders.created.queue` |

Propiedades configurables:

```properties
spring.rabbitmq.host=${RABBITMQ_HOST:localhost}
spring.rabbitmq.port=${RABBITMQ_PORT:5672}
spring.rabbitmq.username=${RABBITMQ_USERNAME:guest}
spring.rabbitmq.password=${RABBITMQ_PASSWORD:guest}

app.rabbitmq.orders.exchange=${ORDERS_EXCHANGE:relatos.orders.exchange}
app.rabbitmq.orders.created-routing-key=${ORDERS_CREATED_ROUTING_KEY:orders.created}
app.rabbitmq.orders.created-queue=${ORDERS_CREATED_QUEUE:relatos.orders.created.queue}
```

## Clases creadas

| Clase | Responsabilidad |
|---|---|
| `RabbitMqConfig` | Declara exchange, queue, binding y converter JSON. |
| `OrderCreatedEvent` | DTO del evento principal. |
| `OrderCreatedItemEvent` | DTO de cada item comprado. |
| `OrderEventPublisher` | Publica el evento con `RabbitTemplate` y registra logs de exito/error. |

## Estructura del evento

```json
{
  "eventId": "uuid",
  "eventType": "OrderCreated",
  "orderId": 4,
  "userId": "1",
  "userEmail": "cliente@relatos.com",
  "status": "CREATED",
  "total": 12.99,
  "items": [
    {
      "bookId": 1,
      "bookTitle": "El principito",
      "quantity": 1,
      "unitPrice": 12.99,
      "subtotal": 12.99
    }
  ],
  "createdAt": "2026-06-19T13:49:00"
}
```

## Flujo implementado

1. `orders-service` valida el JWT interno recibido como `accessToken`.
2. Valida disponibilidad del libro contra `catalogue-service` usando nombre logico Eureka.
3. Persiste la orden y sus items.
4. Construye `OrderCreatedEvent` desde la orden persistida y el usuario autenticado.
5. Publica en `relatos.orders.exchange` con routing key `orders.created`.
6. Si RabbitMQ falla, el pedido queda creado y se registra error de consistencia eventual.

Log esperado en publicacion correcta:

```text
OrderCreated publicado orderId=... userId=... routingKey=orders.created
```

## RabbitMQ local

Para esta fase no se redisenio Docker Compose completo. RabbitMQ puede levantarse temporalmente con:

```powershell
docker run --name rabbitmq-relatos -p 5672:5672 -p 15672:15672 -d rabbitmq:3-management
```

RabbitMQ Management queda disponible en:

```text
http://localhost:15672
usuario: guest
password: guest
```

## Comandos PowerShell para probar

Compilar `orders-service`:

```powershell
Push-Location orders-service
.\mvnw.cmd compile
Pop-Location
```

Levantar RabbitMQ:

```powershell
docker run --name rabbitmq-relatos -p 5672:5672 -p 15672:15672 -d rabbitmq:3-management
```

Reiniciar `orders-service` despues de levantar RabbitMQ:

```powershell
$env:JWT_SECRET="local-dev-secret-change-me-activity-3-relatos-de-papel-32bytes"
Push-Location orders-service
.\mvnw.cmd spring-boot:run
Pop-Location
```

Crear pedido por Gateway con token opaco:

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

Confirmar logs de `orders-service`:

```text
OrderCreated publicado orderId=... userId=... routingKey=orders.created
```

Confirmar en RabbitMQ Management:

- Exchange: `relatos.orders.exchange`;
- Queue: `relatos.orders.created.queue`;
- Binding con routing key `orders.created`.

## Evidencia para video

- Pedido creado correctamente por Gateway.
- Log de `orders-service` mostrando publicacion `OrderCreated`.
- RabbitMQ Management mostrando exchange, queue y binding.
- Explicar que aun no existe consumidor: se implementara en `comms-service` en Fase 5.

## Validacion realizada

Se ejecuto:

```powershell
Push-Location orders-service
.\mvnw.cmd compile
Pop-Location
```

Resultado: `BUILD SUCCESS`.
