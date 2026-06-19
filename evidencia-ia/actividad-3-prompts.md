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
