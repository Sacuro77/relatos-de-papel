# Fase 2 - Gateway Phantom Token

Proyecto: Relatos de Papel  
Modulo modificado: `cloud-gateway`  
Stack: Spring Cloud Gateway Server WebMVC

## Objetivo

Convertir `cloud-gateway` en defensor principal de la arquitectura mediante el patron phantom token:

1. El cliente envia `Authorization: Bearer <opaqueToken>`.
2. Gateway valida el token opaco contra `users-service`.
3. Si el token esta activo, Gateway obtiene el JWT interno.
4. Gateway reenvia la peticion al microservicio destino agregando el header `accessToken: <JWT>`.
5. Si el token falta, tiene formato invalido, esta inactivo o `users-service` no responde, Gateway corta la peticion.

## Endpoints publicos

Estos endpoints pasan sin token opaco:

| Metodo | Ruta | Motivo |
|---|---|---|
| `POST` | `/api/v1/auth/login` | Permite iniciar sesion. |
| `POST` | `/api/v1/auth/refresh` | Permite renovar token opaco. |
| `GET` | `/api/v1/books/**` | Catalogo publico. |
| `GET` | `/catalogue/**` | Ruta legacy temporal de catalogo. |
| `GET` | `/api/catalogue/search` | Ruta legacy temporal de busqueda. |
| Cualquiera | `/actuator/**` | Salud y diagnostico tecnico. |

Nota: `/api/v1/auth/validate` no queda publico en Gateway porque devuelve el JWT interno. El Gateway lo consume de forma interna resolviendo `users-service` por Eureka.

## Endpoints protegidos

Estos endpoints requieren `Authorization: Bearer <opaqueToken>`:

| Ruta | Motivo |
|---|---|
| `/api/v1/orders` y `/api/v1/orders/**` | Opera con pedidos y datos de cliente. |
| `/api/v1/users/profile` | Expone perfil del usuario autenticado. |
| `/orders/**` | Ruta legacy temporal de pedidos. |
| `/api/orders/**` | Ruta legacy temporal de pedidos. |

## Rutas Gateway

La arquitectura de Actividad 3 prioriza `/api/v1/**`:

| Ruta Gateway | Servicio destino |
|---|---|
| `/api/v1/auth/**` | `lb://users-service` |
| `/api/v1/users/**` | `lb://users-service` |
| `/api/v1/books/**` | `lb://catalogue-service` |
| `/api/v1/orders` y `/api/v1/orders/**` | `lb://orders-service` |

Se mantienen rutas legacy de Actividad 2 para no romper pruebas anteriores:

- `/catalogue/**`;
- `/orders/**`;
- `/api/catalogue/search`;
- `/api/orders/create`.

## Implementacion Java

Clases creadas:

| Clase | Responsabilidad |
|---|---|
| `PhantomTokenFilter` | Filtro Servlet formal para Gateway MVC. Detecta rutas publicas/protegidas, valida token opaco e inyecta `accessToken`. |
| `AuthValidationClient` | Cliente interno que resuelve `users-service` con Eureka mediante `LoadBalancerClient` y llama `/api/v1/auth/validate`. |
| `GatewaySecurityProperties` | Propiedades configurables para servicio de validacion, path y nombre del header `accessToken`. |
| `GatewayHttpClientConfig` | Bean `RestClient` usado por el cliente de validacion. |
| `AccessTokenRequestWrapper` | Wrapper de `HttpServletRequest` que agrega el header `accessToken` antes del enrutamiento. |
| `TokenValidationRequest` | DTO enviado a `users-service`. |
| `TokenValidationResponse` | DTO recibido desde `users-service`. |
| `AuthValidationException` | Excepcion controlada ante fallo de validacion. |

## Validacion de token opaco

Gateway espera:

```http
Authorization: Bearer <opaqueToken>
```

Para rutas protegidas:

1. Extrae el token opaco.
2. Resuelve una instancia de `users-service` desde Eureka.
3. Llama a:

```http
POST /api/v1/auth/validate
Content-Type: application/json

{"opaqueToken":"..."}
```

4. Si `active=true` y `accessToken` no esta vacio, continua la peticion.
5. Si no, responde `401`.
6. Si no hay instancia de `users-service` o falla la validacion, responde `503`.

## Inyeccion de accessToken

Cuando la validacion es correcta, Gateway envuelve la peticion original y agrega:

```http
accessToken: <JWT interno>
```

Ese header sera usado en Fase 3 por `orders-service` y ya puede ser usado por `users-service` en `/api/v1/users/profile`.

## Comandos PowerShell para probar

Compilar solo Gateway:

```powershell
Push-Location cloud-gateway
.\mvnw.cmd test
Pop-Location
```

Levantar dependencias de Fase 1 si aun no estan en Compose:

```powershell
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
$env:JWT_SECRET="local-dev-secret-change-me-activity-3-relatos-de-papel-32bytes"
Push-Location users-service
.\mvnw.cmd spring-boot:run
Pop-Location
```

```powershell
Push-Location cloud-gateway
.\mvnw.cmd spring-boot:run
Pop-Location
```

Login mediante Gateway:

```powershell
$login = curl.exe -s -X POST http://localhost:8080/api/v1/auth/login `
  -H "Content-Type: application/json" `
  -d "{\"email\":\"cliente@relatos.com\",\"password\":\"123456\"}" | ConvertFrom-Json

$opaque = $login.opaqueToken
$opaque
```

Endpoint protegido sin token, debe devolver `401`:

```powershell
curl.exe -i http://localhost:8080/api/v1/users/profile
```

Profile protegido mediante Gateway con token opaco:

```powershell
curl.exe -i http://localhost:8080/api/v1/users/profile `
  -H "Authorization: Bearer $opaque"
```

Validar directamente el token opaco mediante Gateway:

```powershell
curl.exe -i -X POST http://localhost:8080/api/v1/auth/validate `
  -H "Content-Type: application/json" `
  -d "{\"opaqueToken\":\"$opaque\"}"
```

Ese comando debe devolver `401` desde Gateway. La validacion real del token opaco ocurre internamente cuando se consume una ruta protegida.

## Logs esperados

En `cloud-gateway` debe aparecer una linea similar:

```text
Token opaco validado para subject=1 email=cliente@relatos.com ruta=/api/v1/users/profile
```

## Evidencia para video

- Eureka con `users-service` y `cloud-gateway` registrados.
- Login por Gateway devolviendo solo token opaco.
- `/api/v1/users/profile` sin token devolviendo `401`.
- `/api/v1/users/profile` con token opaco devolviendo perfil.
- Log de Gateway mostrando validacion del token opaco.

## Validacion realizada

Se ejecuto:

```powershell
Push-Location cloud-gateway
.\mvnw.cmd test
Pop-Location
```

Resultado: `BUILD SUCCESS`.

Durante el test, Eureka no estaba levantado y aparecieron avisos de conexion rechazada contra `localhost:8761`; no bloquearon la compilacion ni el test de contexto.
