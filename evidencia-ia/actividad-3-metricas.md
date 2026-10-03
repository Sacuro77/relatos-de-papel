# Evidencia IA - Actividad 3 - Métricas

Proyecto: Relatos de Papel
Actividad: Actividad 3 - Desarrollo Web: Full Stack

Este documento registra el uso de herramientas de inteligencia artificial como apoyo durante el desarrollo de la Actividad 3. La IA fue utilizada principalmente para diagnóstico técnico, generación inicial de estructuras, apoyo en documentación, comandos de prueba y revisión de errores. Todas las decisiones relevantes fueron revisadas y validadas manualmente mediante lectura de código, compilación, ejecución local, pruebas con `curl`, Docker, RabbitMQ y revisión de logs.

## Porcentaje de respuestas correctas o parcialmente correctas

Valor estimado al cierre:

```text
80 %
```

La mayor parte de las respuestas fueron útiles como punto de partida o como guía técnica. En varias fases fue necesario ajustar comandos, corregir integración entre servicios o adaptar la solución al entorno local de Windows, Maven y Docker.

## Porcentaje de respuestas incorrectas o no aplicables directamente

Valor estimado al cierre:

```text
20 %
```

Las respuestas no aplicables estuvieron relacionadas principalmente con limitaciones del entorno local, dependencias Maven no descargables por errores de certificado PKIX y ajustes técnicos del WebSocket.

## Uso de IA por área

| Área                             | Uso principal de IA                                                       | Resultado                            | Revisión manual |
| -------------------------------- | ------------------------------------------------------------------------- | ------------------------------------ | --------------- |
| Diagnóstico inicial              | Identificación de brechas respecto al enunciado                           | Útil para planificar fases           | Sí              |
| `users-service`                  | Estructura inicial, DTOs, servicios, configuración JWT/Redis              | Funcional y validado                 | Sí              |
| Gateway phantom token            | Diseño del filtro, cliente de validación y rutas protegidas               | Funcional y validado                 | Sí              |
| Protección de `orders-service`   | Validación de JWT interno y eliminación de confianza en `userId` del body | Funcional y validado                 | Sí              |
| RabbitMQ y evento `OrderCreated` | Configuración AMQP, evento y publisher                                    | Funcional y validado                 | Sí              |
| `comms-service`                  | Consumidor RabbitMQ y notificación mock por email                         | Funcional y validado                 | Sí              |
| WebSocket                        | Implementación inicial del endpoint de soporte                            | Compilable, validación local parcial | Sí              |
| Documentación técnica            | Redacción base por fases y evidencia                                      | Ajustada manualmente                 | Sí              |
| Comandos de prueba               | Apoyo para `curl`, Docker, RabbitMQ y Maven                               | Ajustados según errores reales       | Sí              |

## Líneas aproximadas generadas con apoyo de IA

Las siguientes cifras son aproximadas. Incluyen código base generado con asistencia de IA y posteriormente revisado, ajustado o validado manualmente.

| Área                                  | Líneas aproximadas | Revisadas manualmente  |
| ------------------------------------- | -----------------: | ---------------------- |
| `users-service`                       |                600 | Sí                     |
| Filtro Gateway phantom token          |                350 | Sí                     |
| Protección de `orders-service`        |                250 | Sí                     |
| RabbitMQ y evento `OrderCreated`      |                250 | Sí                     |
| `comms-service` consumidor/email mock |                450 | Sí                     |
| WebSocket soporte                     |                150 | Sí, validación parcial |
| Documentación técnica y evidencia     |                500 | Sí                     |

Total estimado:

```text
2550 líneas aproximadas con apoyo de IA
```

## Tiempo estimado ahorrado

| Tarea                                          | Tiempo estimado ahorrado |
| ---------------------------------------------- | -----------------------: |
| Diagnóstico inicial y planificación por fases  |              2 a 3 horas |
| Diseño de `users-service` y phantom token      |              4 a 5 horas |
| Filtro Gateway y validación token opaco        |              3 a 4 horas |
| Protección de `orders-service` con JWT interno |              2 a 3 horas |
| RabbitMQ y publicación de eventos              |              2 a 3 horas |
| `comms-service` consumidor y email mock        |              3 a 4 horas |
| Documentación técnica y evidencia              |              3 a 4 horas |
| Análisis de errores y comandos de prueba       |              2 a 3 horas |

Total estimado:

```text
21 a 29 horas de apoyo acumulado
```

Este tiempo corresponde al apoyo en generación inicial, diagnóstico, documentación y comandos. No reemplaza el tiempo de revisión, ejecución, corrección y validación manual.

## Errores detectados y corregidos manualmente

| Fecha      | Error o limitación detectada                                                               | Corrección o decisión aplicada                                                                 | Archivo o evidencia                                     |
| ---------- | ------------------------------------------------------------------------------------------ | ---------------------------------------------------------------------------------------------- | ------------------------------------------------------- |
| 2026-06-19 | El wrapper Maven de `users-service` no tenía carpeta `.mvn/wrapper`.                       | Se copió la configuración del wrapper desde otro microservicio existente.                      | `users-service/.mvn/wrapper`                            |
| 2026-06-19 | `02-catalogue-dml.sql` no contenía SQL válido, sino código tipo Python.                    | Se insertó manualmente un libro demo en PostgreSQL para validar pedidos.                       | `postgres-catalogue`, tabla `books`                     |
| 2026-06-19 | `users-service` mostraba errores de conexión a Eureka cuando Eureka no estaba levantado.   | Se identificó como advertencia operativa no bloqueante para prueba aislada.                    | Logs de `users-service`                                 |
| 2026-06-19 | Pruebas con JSON en PowerShell fallaban por escape incorrecto de comillas.                 | Se cambió a variables `$loginJson`, `$orderJson` y `--data-raw`.                               | Pruebas `curl`                                          |
| 2026-06-19 | `orders-service` no debía confiar en `userId` enviado por el cliente.                      | Se ajustó la lógica para usar el `sub` del JWT interno recibido en `accessToken`.              | `InternalJwtService.java`, `OrderService.java`          |
| 2026-06-19 | RabbitMQ enviaba header `__TypeId__` con paquete Java del productor.                       | Se ajustó `comms-service` para consumir el body JSON y mapearlo a su DTO local.                | `RabbitMqConfig.java`, `OrderCreatedListener.java`      |
| 2026-06-19 | Maven no pudo descargar dependencias de Spring Mail por error PKIX.                        | Se mantuvo modo local `MAIL_ENABLED=false` con `MAIL MOCK` y cliente SMTP básico configurable. | `EmailNotificationService.java`, `SmtpEmailClient.java` |
| 2026-06-19 | WebSocket presentó problemas de validación local por dependencias y registro del endpoint. | Se dejó implementación compilable y documentada; se registra como validación parcial.          | `WebSocketConfig.java`, `SupportChatEndpoint.java`      |

## Validaciones manuales realizadas

Se realizaron validaciones manuales por fase:

| Fase             | Validación realizada                                           | Resultado |
| ---------------- | -------------------------------------------------------------- | --------- |
| `users-service`  | Login, validate, refresh y profile                             | Correcto  |
| Gateway          | Profile sin token `401`; profile con token opaco `200`         | Correcto  |
| `orders-service` | Pedido con `usuario-falso` se guardó con `userId` real del JWT | Correcto  |
| Catalogue        | Libro demo consultado directo y vía Gateway                    | Correcto  |
| RabbitMQ         | Exchange, queue y binding creados                              | Correcto  |
| `comms-service`  | Cola `relatos.orders.created.queue` quedó en `0` tras consumir | Correcto  |
| Email mock       | Registro de notificación simulada en logs                      | Correcto  |
| WebSocket        | Implementación compilable; validación local no completada      | Parcial   |

## Observaciones finales

La IA se utilizó como herramienta de apoyo, no como sustituto del desarrollo ni de la validación. Cada cambio fue revisado manualmente antes de aceptarse.

Las principales decisiones técnicas tomadas manualmente fueron:

* mantener Spring Cloud Gateway MVC y no migrar a WebFlux;
* implementar un filtro Java formal para el phantom token;
* no exponer el JWT interno al cliente;
* ignorar el `userId` enviado por el cliente en pedidos;
* mantener el pedido creado aunque RabbitMQ falle, documentando consistencia eventual;
* usar email mock local para no depender de credenciales reales;
* documentar honestamente la validación parcial del WebSocket.

La evidencia principal de funcionamiento se concentra en el flujo:

```text
Login -> token opaco -> Gateway -> users-service validate -> accessToken interno -> orders-service protegido -> OrderCreated -> RabbitMQ -> comms-service -> MAIL MOCK
```
