# Diagnostico Inicial - Actividad 3

Proyecto: Relatos de Papel  
Contexto: continuacion directa de la Actividad 2 del master Desarrollo Web: Full Stack.

## Estructura actual del proyecto

```text
Actividad_3_Backend_Relatos_Papel/
├── catalogue-service/
├── cloud-gateway/
├── eureka-server/
├── orders-service/
├── sql/
├── evidencia-ia/
└── docker-compose.yml
```

No se observa frontend React dentro de este arbol de trabajo. Al inicio de Fase 0 tampoco existian los microservicios `users-service` ni `comms-service`.

## Servicios existentes

| Servicio | Estado actual | Funcion principal |
|---|---|---|
| `eureka-server` | Existente | Registro y descubrimiento de servicios con Eureka. |
| `catalogue-service` | Existente | CRUD, busqueda y disponibilidad de libros. |
| `orders-service` | Existente | Registro y consulta de pedidos. |
| `cloud-gateway` | Existente | Enrutamiento hacia catalogue y orders mediante Spring Cloud Gateway Server WebMVC. |
| `users-service` | Creado en Fase 1, pendiente de validacion runtime | Autenticacion, JWT interno, token opaco y validacion para Gateway. |

## Servicios faltantes

| Servicio/componente | Estado inicial | Necesidad para Actividad 3 |
|---|---|---|
| `comms-service` | No existe | Consumidor RabbitMQ, envio SMTP/Gmail, WebSocket y Gemini. |
| Redis | Configurado en `users-service`, pendiente de levantar en Docker/Compose | Almacenamiento token opaco -> JWT para patron phantom token. |
| RabbitMQ | No configurado | Broker para evento `OrderCreated`. |
| Frontend React | No localizado en este repo | Debe consumir exclusivamente por Gateway. |
| Dockerfiles | No existen | Necesarios para despliegue local completo y remoto. |
| Railway/Vercel | No configurado | Necesario para despliegue remoto publico. |

## Estado actual de Gateway

El modulo `cloud-gateway` usa Spring Cloud Gateway Server WebMVC, no WebFlux. Las rutas estan declaradas en `cloud-gateway/src/main/resources/application.properties`.

Rutas existentes:

| Ruta externa | Destino | Filtro actual |
|---|---|---|
| `/catalogue/**` | `lb://catalogue-service` | `StripPrefix=1` |
| `/orders/**` | `lb://orders-service` | `StripPrefix=1` |
| `/api/catalogue/search` | `lb://catalogue-service` | `RewritePath=/api/catalogue/search,/api/v1/books/search` |
| `/api/orders/create` | `lb://orders-service` | `RewritePath=/api/orders/create,/api/v1/orders` |

Estado de seguridad:

- No existe filtro Java formal para autenticacion/autorizacion.
- No existe distincion formal entre endpoints publicos y protegidos.
- No se valida token opaco contra `users-service`.
- No se inyecta header `accessToken` con JWT hacia servicios internos.
- El Gateway usa nombres Eureka para enrutar a servicios existentes.

## Estado actual de orders-service

`orders-service` contiene:

- controlador REST `OrderController`;
- servicio de negocio `OrderService`;
- repositorio `PurchaseOrderRepository`;
- entidades `PurchaseOrder` y `OrderItem`;
- DTOs de solicitud/respuesta;
- cliente HTTP `CatalogueClient`;
- configuracion `WebClientConfig`.

La comunicacion con catalogue se realiza con `WebClient.Builder` anotado con `@LoadBalanced` y URL logica `http://catalogue-service/...`, por lo que esta alineada con Eureka.

Riesgo actual:

- La identidad del usuario se toma desde `userId` en body/path.
- No se lee ni valida el header `accessToken`.
- No se publica evento `OrderCreated`.
- No existe integracion con RabbitMQ.

## Estado actual de users-service

Fase 1 creo un nuevo modulo `users-service` con Java 17, Spring Boot 4.0.6 y Spring Cloud 2025.1.1, alineado con los demas servicios.

Estado implementado pendiente de validacion runtime:

- nombre Eureka `users-service`;
- puerto local `8083`;
- datasource PostgreSQL local previsto en `localhost:5435/users_db`;
- Redis local previsto en `localhost:6379`;
- endpoint `POST /api/v1/auth/login`;
- endpoint `POST /api/v1/auth/validate`;
- endpoint `POST /api/v1/auth/refresh`;
- endpoint `GET /api/v1/users/profile`;
- generacion de JWT interno con claims `sub`, `email`, `roles`, `iat`, `exp` e `iss`;
- generacion de token opaco externo;
- almacenamiento `opaqueToken -> JWT` en Redis con TTL;
- usuario demo `cliente@relatos.com` con contrasena `123456` hasheada mediante BCrypt por seeder runtime;
- scripts SQL `05-users-ddl.sql` y `06-users-dml.sql`.

Pendiente:

- levantar PostgreSQL users y Redis en Docker Compose final;
- compilar y probar cuando se autorice ejecutar Maven;
- integrar validacion desde Gateway en Fase 2.

## Estado actual de catalogue-service

`catalogue-service` contiene:

- controlador REST `BookController`;
- servicio `BookService`;
- repositorio `BookRepository`;
- entidad `Book`;
- DTOs `BookRequest`, `BookResponse` y `BookAvailabilityResponse`.

Estado funcional:

- CRUD de libros implementado.
- Endpoint de busqueda implementado.
- Endpoint de disponibilidad implementado para que orders valide libros.
- `BookRepository` extiende `JpaSpecificationExecutor`.

Observacion:

- La `Specification` de busqueda esta embebida en `BookService`; no existe clase `Specification` separada. Esto no bloquea la Actividad 3, pero es una mejora formal posible si se decide reforzar catalogue.

## Estado actual de Docker

`docker-compose.yml` levanta unicamente:

- `postgres-catalogue`;
- `postgres-orders`;
- volumen `catalogue_data`;
- volumen `orders_data`.

No levanta:

- Eureka;
- Gateway;
- catalogue-service;
- orders-service;
- users-service;
- comms-service;
- Redis;
- RabbitMQ;
- frontend.

No existen Dockerfiles por microservicio.

## Riesgos principales detectados

1. `users-service` fue creado en Fase 1, pero aun falta validacion runtime y conexion desde Gateway.
2. Phantom token esta implementado en `users-service`, pero falta filtro Java en Gateway.
3. Gateway carece de filtro Java formal, punto critico por la retroalimentacion de Actividad 2.
4. Endpoints de datos de cliente en orders no estan protegidos.
5. `orders-service` acepta `userId` desde la peticion, lo que permite suplantacion si no se corrige.
6. No existe RabbitMQ ni evento `OrderCreated`.
7. No existe `comms-service`, SMTP/Gmail, WebSocket ni Gemini.
8. Docker Compose no representa la arquitectura final exigida.
9. Hay configuraciones locales con `localhost` que deberan parametrizarse para Docker y Railway.
10. La evidencia IA existente corresponde a Actividad 2 y debe ampliarse para Actividad 3.
11. `sql/02-catalogue-dml.sql` debe revisarse porque su contenido no esta en formato SQL final ejecutable.
12. Los tests actuales son basicos y no cubren seguridad, eventos ni contratos entre servicios.

## Conclusion tecnica

El proyecto tiene una base valida de Actividad 2: Eureka, catalogue, orders, Gateway y persistencia PostgreSQL para catalogue/orders. Para Actividad 3 el trabajo principal no debe ser refactorizar lo existente, sino completar formalmente la arquitectura exigida: `users-service`, phantom token en Gateway mediante filtro Java, proteccion real de orders, eventos RabbitMQ, `comms-service`, correo, WebSocket, Gemini, Docker Compose completo, despliegue remoto y evidencia documentada.

La prioridad tecnica es implementar requisitos evaluables de forma explicita y demostrable, evitando soluciones solo declarativas cuando el enunciado exige comportamiento formal.
