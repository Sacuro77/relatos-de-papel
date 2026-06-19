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
| Filtro Gateway | Pendiente | Pendiente |
| Proteccion orders | Pendiente | Pendiente |
| RabbitMQ/eventos | Pendiente | Pendiente |
| `comms-service` SMTP | Pendiente | Pendiente |
| WebSocket | Pendiente | Pendiente |
| Gemini | Pendiente | Pendiente |
| Docker/Compose | Pendiente | Pendiente |
| Documentacion | 90 | Si |

Total estimado:

```text
1040 lineas aproximadas
```

## Tiempo estimado ahorrado

Pendiente de completar.

Detalle sugerido:

| Tarea | Tiempo estimado ahorrado |
|---|---:|
| Diagnostico inicial | Pendiente |
| Diseno users/auth | 4 a 6 horas |
| Gateway phantom token | Pendiente |
| RabbitMQ/comms | Pendiente |
| Docker/despliegue | Pendiente |
| Documentacion | Pendiente |

Total estimado:

```text
4 a 6 horas en Fase 1, pendiente total final
```

## Errores detectados y corregidos manualmente

Pendiente de completar.

| Fecha | Error o respuesta incompleta de IA | Correccion manual realizada | Archivo o evidencia |
|---|---|---|---|
| 2026-06-19 | No se pudo generar BCrypt con herramientas locales disponibles. | Se uso `PasswordEncoder` en seeder runtime y `pgcrypto` en SQL para insertar hash BCrypt sin almacenar contrasena plana. | `DemoUserSeeder.java`, `sql/06-users-dml.sql` |

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
