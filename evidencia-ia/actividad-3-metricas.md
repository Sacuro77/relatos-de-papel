# Evidencia IA - Actividad 3 - Metricas

Proyecto: Relatos de Papel  
Actividad: Actividad 3 - Desarrollo Web: Full Stack

Este documento se completara durante el desarrollo de la Actividad 3.

## Porcentaje de respuestas correctas o parcialmente correctas

Pendiente de completar.

Valor estimado al cierre:

```text
__ %
```

## Porcentaje de respuestas incorrectas

Pendiente de completar.

Valor estimado al cierre:

```text
__ %
```

## Lineas aproximadas generadas por IA

Pendiente de completar.

Detalle sugerido:

| Area | Lineas aproximadas | Revisadas manualmente |
|---|---:|---|
| `users-service` | 950 | Si, pendiente de compilacion |
| Filtro Gateway | 430 | Si, compilado con `cloud-gateway` |
| Proteccion orders | 260 | Si, compilado con `orders-service` |
| RabbitMQ/eventos | 260 | Si, compilado con `orders-service` |
| `comms-service` SMTP | 650 | Si, compilado en modo mock/log y SMTP basico |
| WebSocket | Pendiente | Pendiente |
| Gemini | Pendiente | Pendiente |
| Docker/Compose | Pendiente | Pendiente |
| Documentacion | 90 | Si |

Total estimado:

```text
2640 lineas aproximadas
```

## Tiempo estimado ahorrado

Pendiente de completar.

Detalle sugerido:

| Tarea | Tiempo estimado ahorrado |
|---|---:|
| Diagnostico inicial | Pendiente |
| Diseno users/auth | 4 a 6 horas |
| Gateway phantom token | 3 a 5 horas |
| Proteccion orders | 2 a 4 horas |
| RabbitMQ/comms | 4 a 6 horas acumuladas en publicacion y consumidor |
| Docker/despliegue | Pendiente |
| Documentacion | Pendiente |

Total estimado:

```text
13 a 21 horas acumuladas en Fases 1 a 5, pendiente total final
```

## Errores detectados y corregidos manualmente

Pendiente de completar.

| Fecha | Error o respuesta incompleta de IA | Correccion manual realizada | Archivo o evidencia |
|---|---|---|---|
| 2026-06-19 | No se pudo generar BCrypt con herramientas locales disponibles. | Se uso `PasswordEncoder` en seeder runtime y `pgcrypto` en SQL para insertar hash BCrypt sin almacenar contrasena plana. | `DemoUserSeeder.java`, `sql/06-users-dml.sql` |
| 2026-06-19 | Gateway test intento conectarse a Eureka no levantado durante `mvnw test`. | Se verifico que era un aviso no bloqueante: el resultado final fue `BUILD SUCCESS`. | `cloud-gateway` logs de test |
| 2026-06-19 | `orders-service` test intento conectar a PostgreSQL orders no levantado. | Se valido compilacion con `mvnw compile`; test de contexto queda condicionado a levantar PostgreSQL `localhost:5434`. | `orders-service` logs de test |
| 2026-06-19 | Maven no pudo descargar `spring-boot-starter-mail` por certificado PKIX. | Se dejo `comms-service` compilable con modo mock/log y cliente SMTP basico con Java estandar; Spring Mail queda pendiente tecnico. | `comms-service`, `FASE_5_COMMS_SERVICE_EMAIL.md` |

## Observaciones de validacion manual

Pendiente de completar.

Cada implementacion generada con apoyo de IA debera ser revisada antes de aceptarse mediante:

- lectura manual del codigo;
- pruebas locales;
- verificacion de logs;
- pruebas curl o cliente WebSocket;
- evidencia en video o capturas;
- confirmacion de que los servicios se registran en Eureka;
- confirmacion de que el frontend consume mediante Gateway cuando exista.
