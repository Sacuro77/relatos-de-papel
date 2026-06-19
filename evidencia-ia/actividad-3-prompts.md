# Evidencia IA - Actividad 3 - Prompts

Proyecto: Relatos de Papel  
Actividad: Actividad 3 - Desarrollo Web: Full Stack

## Prompt de diagnostico usado

```text
Actua como arquitecto de software, experto en Spring Boot microservices, Spring Cloud Gateway, Eureka, RabbitMQ, Redis, JWT, WebSocket y despliegue Docker/Railway.

Estoy trabajando la Actividad 3 de Desarrollo Web: Full Stack del master, continuacion directa de la Actividad 2 del proyecto Relatos de Papel.

No implementes todavia. No modifiques archivos. No crees codigo. No hagas refactor. Solo diagnostica el proyecto actual y propon un plan tecnico exacto.

Revisa estructura, versiones, Gateway, rutas, filtros, comunicacion entre servicios, uso de localhost, controllers, services, repositories, entities, DTOs, docker-compose, Dockerfiles, sql, evidencia-ia, riesgos, arquitectura objetivo y plan por fases.
```

## Resumen de respuesta obtenida

La respuesta identifico que el proyecto actual contiene cuatro modulos principales heredados de Actividad 2:

- `eureka-server`;
- `catalogue-service`;
- `orders-service`;
- `cloud-gateway`.

Tambien identifico que:

- todos los POMs usan Java 17, Spring Boot 4.0.6 y Spring Cloud 2025.1.1;
- el Gateway usa Spring Cloud Gateway Server WebMVC;
- las rutas del Gateway estan configuradas en `application.properties`;
- no existe filtro Java formal en Gateway;
- `orders-service` llama a `catalogue-service` con nombre logico Eureka mediante `WebClient` balanceado;
- no existen `users-service`, `comms-service`, Redis, RabbitMQ, WebSocket, Gemini ni Docker Compose completo;
- la evidencia IA existente corresponde a Actividad 2;
- la prioridad tecnica debe ser implementar primero los requisitos obligatorios y dejar Elasticsearch como extra opcional.

## Decision tomada

Se decidio iniciar la Actividad 3 con una Fase 0 estrictamente documental para:

- dejar registrado el diagnostico inicial;
- crear una matriz de cumplimiento contra la rubrica;
- definir un plan tecnico por fases;
- preparar evidencia IA propia de la Actividad 3;
- evitar modificaciones prematuras en codigo fuente, configuracion o dependencias.

Esta decision responde al aprendizaje de Actividad 2: los requisitos formales deben quedar implementados y evidenciados de forma explicita, no solo funcional.

## Nota de validacion

La implementacion sera revisada manualmente antes de aceptarse.

## Prompt de implementacion Fase 1

```text
Implementa unicamente la Fase 1 de la Actividad 3: creacion de users-service con PostgreSQL, Redis, JWT y token opaco.

No modifiques catalogue-service, orders-service, cloud-gateway, eureka-server ni docker-compose.yml. No implementes RabbitMQ, comms-service, WebSocket, Gemini ni Elasticsearch. No hagas commit.

El servicio debe usar Java 17, Spring Boot 4.0.6, Spring Cloud 2025.1.1, puerto 8083, nombre Eureka users-service, PostgreSQL users_db, Redis, BCrypt y variables de entorno para secretos. Debe exponer login, validate, refresh y profile. El login devuelve solo token opaco. Validate devuelve JWT interno si el token opaco esta activo. Profile lee header accessToken.
```

## Resumen de respuesta Fase 1

Se genero un nuevo modulo `users-service` con:

- POM Maven alineado con Java 17, Spring Boot 4.0.6 y Spring Cloud 2025.1.1;
- configuracion local de PostgreSQL, Redis, Eureka y JWT;
- entidad `AppUser` y tabla auxiliar de roles;
- repositorio `UserRepository`;
- endpoints de autenticacion y perfil;
- servicios `AuthService`, `JwtService` y `OpaqueTokenService`;
- almacenamiento de token opaco en Redis con prefijo `phantom:token:`;
- seeder runtime para usuario demo con contrasena BCrypt;
- scripts SQL `05-users-ddl.sql` y `06-users-dml.sql`.

## Decision tomada Fase 1

Se decidio no activar seguridad web global en esta fase para no interferir con los endpoints publicos de autenticacion antes de implementar el Gateway defensor en Fase 2. La validacion de `accessToken` se hace explicitamente en el endpoint de perfil.

Tambien se decidio no modificar `docker-compose.yml` todavia. PostgreSQL users y Redis quedan como pendientes operativos para levantar con comandos Docker temporales o integrarlos en Compose durante Fase 8.

## Prompt de implementacion Fase 2

```text
Implementa unicamente la Fase 2 de la Actividad 3: filtro Java formal en cloud-gateway para phantom token.

No modifiques catalogue-service, orders-service, users-service ni eureka-server. Mantén compatibilidad con Spring Cloud Gateway Server WebMVC. El Gateway debe distinguir rutas publicas y protegidas, leer Authorization: Bearer <opaqueToken>, llamar a users-service /api/v1/auth/validate, inyectar accessToken con JWT interno y responder 401 o 503 de forma controlada.
```

## Resumen de respuesta Fase 2

Se implemento en `cloud-gateway`:

- filtro Java formal `PhantomTokenFilter` compatible con Gateway MVC;
- cliente `AuthValidationClient` para resolver `users-service` via Eureka con `LoadBalancerClient`;
- DTOs de request/response para validacion del token opaco;
- wrapper `AccessTokenRequestWrapper` para agregar el header `accessToken`;
- propiedades `gateway.security.*`;
- rutas `/api/v1/auth/**`, `/api/v1/users/**`, `/api/v1/books/**` y `/api/v1/orders/**`;
- mantenimiento temporal de rutas legacy de Actividad 2.

## Decision tomada Fase 2

Se uso un filtro Servlet `OncePerRequestFilter` porque el proyecto usa Spring Cloud Gateway Server WebMVC. Esta decision evita usar APIs reactivas de WebFlux que no corresponden al stack actual.

Para validar contra `users-service`, se uso `LoadBalancerClient` y el nombre logico `users-service`, evitando `localhost:8083` como solucion final.

## Prompt de implementacion Fase 3

```text
Implementa unicamente la Fase 3 de la Actividad 3: proteger orders-service usando el JWT interno recibido en el header accessToken.

No modifiques catalogue-service, users-service, cloud-gateway salvo ajuste estrictamente necesario, ni eureka-server. Orders debe rechazar operaciones sin accessToken, validar JWT con el mismo JWT_SECRET e issuer que users-service, extraer sub/email/roles, ignorar userId enviado por el cliente y asociar las ordenes al usuario autenticado.
```

## Resumen de respuesta Fase 3

Se implemento en `orders-service`:

- dependencia JWT compatible con `users-service`;
- propiedades `app.jwt.secret`, `app.jwt.issuer` y `app.security.access-token-header`;
- clase `InternalJwtService` para validar firma, issuer y expiracion;
- clase `AuthenticatedUser` con `userId`, `email` y `roles`;
- excepcion `AuthenticationException` con respuesta `401`;
- proteccion de todos los endpoints de pedidos mediante header `accessToken`;
- nueva ruta `GET /api/v1/orders/recent`;
- mantenimiento de `GET /api/v1/orders/recent/{userId}` ignorando el path y usando el JWT;
- creacion de pedidos usando el claim `sub`, no el body.

## Decision tomada Fase 3

Se mantuvo temporalmente el campo `userId` en `OrderRequest` como obsoleto para no romper clientes antiguos, pero se elimino del flujo de negocio. El usuario real de la orden siempre sale del JWT interno.

Se realizo un ajuste minimo en `cloud-gateway` para proteger y enrutar tambien la ruta base `POST /api/v1/orders`, necesaria para la API final.

## Prompt de implementacion Fase 4

```text
Implementa unicamente la Fase 4 de la Actividad 3: publicacion de evento OrderCreated desde orders-service hacia RabbitMQ.

No modifiques catalogue-service, users-service, cloud-gateway ni eureka-server. No implementes comms-service ni consumidor todavia. Cuando orders-service cree correctamente un pedido, debe publicar OrderCreated con RabbitTemplate en RabbitMQ. Si RabbitMQ falla, no debe romper la creacion del pedido ya persistido; debe registrar log claro y documentar consistencia eventual.
```

## Resumen de respuesta Fase 4

Se implemento en `orders-service`:

- dependencia `spring-boot-starter-amqp`;
- propiedades RabbitMQ locales configurables por entorno;
- exchange `relatos.orders.exchange`;
- routing key `orders.created`;
- queue `relatos.orders.created.queue`;
- configuracion `RabbitMqConfig`;
- DTO principal `OrderCreatedEvent`;
- DTO de items `OrderCreatedItemEvent`;
- publisher `OrderEventPublisher`;
- publicacion despues de `purchaseOrderRepository.save(order)`.

## Decision tomada Fase 4

Se decidio capturar errores `AmqpException` dentro del publisher para que RabbitMQ no revierta una orden ya persistida. Esto deja una ventana de consistencia eventual que sera mitigada mas adelante con consumidor, reintentos o outbox si se requiere mas robustez.
