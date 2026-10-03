# Relatos de Papel — Actividad 3

Proyecto académico de **Desarrollo Web: Full Stack** basado en una arquitectura de microservicios para una librería online.

Esta rama contiene el desarrollo correspondiente a la **Actividad 3**, construido sobre la base de la Actividad 2.

## Arquitectura implementada

```text
Cliente
   |
   v
Cloud Gateway
   |
   +--> Users Service
   |      +--> PostgreSQL
   |      +--> Redis
   |
   +--> Catalogue Service
   |
   +--> Orders Service
   |      |
   |      +--> OrderCreated
   |              |
   |              v
   |          RabbitMQ
   |              |
   |              v
   +--------> Comms Service
                  |
                  +--> MAIL MOCK
                  +--> WebSocket de soporte

Eureka Server
   └── descubrimiento de microservicios
```

## Funcionalidades desarrolladas

- Arquitectura de microservicios con Spring Boot.
- Service Discovery mediante Eureka.
- API Gateway como punto de entrada.
- `users-service` para autenticación y gestión de identidad.
- Implementación del patrón **Phantom Token**.
- Token opaco entregado al cliente.
- Validación del token opaco en Gateway.
- JWT interno reenviado mediante el header `accessToken`.
- Protección de endpoints de pedidos.
- Identidad del usuario obtenida desde el JWT interno y no desde el `userId` enviado por el cliente.
- Publicación del evento `OrderCreated`.
- Comunicación asíncrona mediante RabbitMQ.
- `comms-service` consumidor de eventos.
- Notificación local mediante `MAIL MOCK`.
- WebSocket para soporte/chat.
- PostgreSQL para persistencia.
- Redis como soporte del mecanismo de autenticación.

## Flujo principal

```text
Login
  ↓
Token opaco
  ↓
Gateway
  ↓
users-service / validate
  ↓
JWT interno (accessToken)
  ↓
orders-service protegido
  ↓
OrderCreated
  ↓
RabbitMQ
  ↓
comms-service
  ↓
MAIL MOCK
```

## Servicios

| Componente | Función |
|---|---|
| `eureka-server` | Registro y descubrimiento de servicios |
| `cloud-gateway` | Entrada al sistema y validación del token opaco |
| `users-service` | Autenticación, identidad y validación de tokens |
| `catalogue-service` | Gestión y consulta del catálogo |
| `orders-service` | Gestión protegida de pedidos |
| `comms-service` | Eventos, notificaciones y soporte WebSocket |
| PostgreSQL | Persistencia de datos |
| Redis | Soporte del mecanismo de autenticación |
| RabbitMQ | Mensajería asíncrona |

## Estado de validación

| Funcionalidad | Estado |
|---|---|
| Login y autenticación | ✅ Validado |
| Phantom Token | ✅ Validado |
| Gateway sin token devuelve 401 | ✅ Validado |
| Gateway con token válido | ✅ Validado |
| Protección de pedidos | ✅ Validado |
| Identidad obtenida desde JWT | ✅ Validado |
| Publicación de eventos RabbitMQ | ✅ Implementada |
| Consumo de eventos RabbitMQ | ✅ Validado |
| Notificación `MAIL MOCK` | ✅ Validado |
| WebSocket de soporte | 🟡 Implementado, validación parcial |
| Docker Compose integral | 🟡 Pendiente de completar |
| Despliegue remoto público | 🟡 Pendiente |
| Integración Gemini | 🟡 Pendiente / no cerrada |

## Evidencia y documentación

La documentación técnica de la Actividad 3 se encuentra en:

```text
docs/actividad3/
```

La evidencia del uso de inteligencia artificial se encuentra en:

```text
evidencia-ia/
```

Incluye métricas, validaciones manuales, errores detectados, correcciones aplicadas y decisiones técnicas tomadas durante el desarrollo.

## Documentación adicional

La planificación y documentación inicial de la actividad se conserva en:

[`README_ACTIVIDAD_3.md`](README_ACTIVIDAD_3.md)

## Ramas del proyecto

| Rama | Contenido |
|---|---|
| `main` | Actividad 1 |
| `actividad-02` | Actividad 2 |
| `actividad-03` | Actividad 3 |

## Repositorio

**Relatos de Papel**  
https://github.com/Sacuro77/relatos-de-papel

---

Proyecto académico desarrollado como parte del Máster en Desarrollo Web: Full Stack.
