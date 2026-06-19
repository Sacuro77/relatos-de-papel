# Fase 3 - Seguridad en orders-service

Proyecto: Relatos de Papel  
Modulo principal modificado: `orders-service`

## Objetivo

Proteger `orders-service` para que opere exclusivamente con el JWT interno que el Gateway inyecta en el header:

```http
accessToken: <JWT interno>
```

Con este cambio, el cliente ya no puede elegir el `userId` de una compra. La identidad del usuario se obtiene siempre del claim `sub` del JWT emitido por `users-service`.

## Flujo de seguridad

1. El cliente inicia sesion por Gateway y recibe un token opaco.
2. El cliente llama a una ruta protegida de pedidos con `Authorization: Bearer <opaqueToken>`.
3. `cloud-gateway` valida el token opaco contra `users-service`.
4. Si el token esta activo, Gateway reenvia la peticion a `orders-service` con `accessToken`.
5. `orders-service` valida firma, issuer y expiracion del JWT.
6. `orders-service` extrae `sub`, `email` y `roles`.
7. La orden se guarda usando el `sub` del JWT como `userId`.

## Cambios principales

| Area | Cambio |
|---|---|
| DTO de creacion | `OrderRequest.userId` queda obsoleto y ya no participa en el flujo de negocio. |
| Controller | Todos los endpoints de pedidos leen `accessToken` y autentican antes de ejecutar negocio. |
| Service | `OrderService.create` recibe `AuthenticatedUser` y guarda `authenticatedUser.userId`. |
| Consulta reciente | Nueva ruta final `GET /api/v1/orders/recent` para consultar pedidos del usuario autenticado. |
| Ruta legacy | `GET /api/v1/orders/recent/{userId}` se mantiene, pero ignora el `userId` del path y usa el JWT. |
| Consulta por id | `GET /api/v1/orders/{orderId}` valida que la orden pertenezca al usuario autenticado. |
| Excepciones | Fallos de autenticacion devuelven `401`. |

## Clases creadas

| Clase | Responsabilidad |
|---|---|
| `AuthenticatedUser` | Modelo interno con `userId`, `email` y `roles` extraidos del JWT. |
| `AuthenticationException` | Excepcion para JWT ausente, invalido o expirado. |
| `InternalJwtService` | Valida firma, issuer y expiracion del JWT interno. |

## Configuracion JWT

`orders-service` usa las mismas propiedades que `users-service`:

```properties
app.jwt.secret=${JWT_SECRET:local-dev-secret-change-me-activity-3-relatos-de-papel-32bytes}
app.jwt.issuer=${JWT_ISSUER:relatos-de-papel-users-service}
app.security.access-token-header=accessToken
```

En despliegue real, `JWT_SECRET` debe definirse como variable de entorno y ser igual en `users-service` y `orders-service`.

## Por que no se acepta userId del cliente

Antes, la orden se creaba con `request.getUserId()`. Eso permitia que un cliente enviara un `userId` arbitrario y creara o consultara datos de otra persona.

Ahora:

- el body puede contener `userId` por compatibilidad temporal, pero se ignora;
- el `userId` real se toma del claim `sub`;
- `GET /recent/{userId}` tambien ignora el path y usa el JWT;
- `GET /{orderId}` verifica pertenencia de la orden al usuario autenticado.

## Comandos PowerShell para probar

Compilar solo `orders-service`:

```powershell
Push-Location orders-service
.\mvnw.cmd compile
Pop-Location
```

Nota: `.\mvnw.cmd test` arranca contexto JPA y requiere PostgreSQL orders activo en `localhost:5434`.

Levantar dependencias temporales si no estan activas:

```powershell
docker run --name postgres-catalogue -e POSTGRES_DB=catalogue_db -e POSTGRES_USER=catalogue_user -e POSTGRES_PASSWORD=catalogue_pass -p 5433:5432 -d postgres:16
docker run --name postgres-orders -e POSTGRES_DB=orders_db -e POSTGRES_USER=orders_user -e POSTGRES_PASSWORD=orders_pass -p 5434:5432 -d postgres:16
docker run --name postgres-users -e POSTGRES_DB=users_db -e POSTGRES_USER=users_user -e POSTGRES_PASSWORD=users_pass -p 5435:5432 -d postgres:16
docker run --name redis-users -p 6379:6379 -d redis:7
```

Levantar servicios en terminales separadas:

```powershell
Push-Location eureka-server
.\mvnw.cmd spring-boot:run
Pop-Location
```

```powershell
Push-Location catalogue-service
.\mvnw.cmd spring-boot:run
Pop-Location
```

```powershell
$env:JWT_SECRET="local-dev-secret-change-me-activity-3-relatos-de-papel-32bytes"
Push-Location users-service
.\mvnw.cmd spring-boot:run
Pop-Location
```

```powershell
$env:JWT_SECRET="local-dev-secret-change-me-activity-3-relatos-de-papel-32bytes"
Push-Location orders-service
.\mvnw.cmd spring-boot:run
Pop-Location
```

```powershell
Push-Location cloud-gateway
.\mvnw.cmd spring-boot:run
Pop-Location
```

Login por Gateway:

```powershell
$login = curl.exe -s -X POST http://localhost:8080/api/v1/auth/login `
  -H "Content-Type: application/json" `
  -d "{\"email\":\"cliente@relatos.com\",\"password\":\"123456\"}" | ConvertFrom-Json

$opaque = $login.opaqueToken
```

Crear pedido sin token, debe devolver `401` en Gateway:

```powershell
curl.exe -i -X POST http://localhost:8080/api/v1/orders `
  -H "Content-Type: application/json" `
  -d "{\"items\":[{\"bookId\":1,\"quantity\":1}]}"
```

Crear pedido con token opaco:

```powershell
curl.exe -i -X POST http://localhost:8080/api/v1/orders `
  -H "Authorization: Bearer $opaque" `
  -H "Content-Type: application/json" `
  -d "{\"userId\":\"usuario-falso\",\"items\":[{\"bookId\":1,\"quantity\":1}]}"
```

Aunque el body envie `usuario-falso`, la respuesta debe mostrar el `userId` del JWT, normalmente `1`.

Consultar pedidos recientes del usuario autenticado:

```powershell
curl.exe -i http://localhost:8080/api/v1/orders/recent `
  -H "Authorization: Bearer $opaque"
```

Probar llamada directa a `orders-service` sin `accessToken`, debe devolver `401`:

```powershell
curl.exe -i -X POST http://localhost:8082/api/v1/orders `
  -H "Content-Type: application/json" `
  -d "{\"items\":[{\"bookId\":1,\"quantity\":1}]}"
```

## Evidencia para video

- Login por Gateway y obtencion de token opaco.
- Pedido sin token devuelve `401`.
- Pedido con token opaco se crea correctamente.
- Body con `userId` falso no altera el usuario de la orden.
- Consulta `/api/v1/orders/recent` devuelve pedidos del usuario autenticado.
- Llamada directa a `orders-service` sin `accessToken` devuelve `401`.

## Validacion realizada

Se ejecuto:

```powershell
Push-Location orders-service
.\mvnw.cmd compile
Pop-Location
```

Resultado: `BUILD SUCCESS`.

Tambien se intento `.\mvnw.cmd test`; la compilacion de clases fue correcta, pero el test de contexto fallo porque PostgreSQL orders no estaba escuchando en `localhost:5434`.
