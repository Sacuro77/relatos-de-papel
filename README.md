# Relatos de Papel - Actividad 3

Proyecto academico del master Desarrollo Web: Full Stack.  
Esta Actividad 3 continua directamente el trabajo de la Actividad 2, que ya dejo una base con Eureka, catalogue, orders, Gateway, PostgreSQL y evidencia inicial de IA.

## Descripcion del proyecto

Relatos de Papel es una arquitectura de microservicios para una libreria online. El sistema debe permitir consultar catalogo, registrar pedidos, autenticar usuarios, proteger datos de cliente, emitir eventos asincronos, enviar comunicaciones y exponer un chat de soporte con integracion de IA.

Estado inicial heredado:

- `eureka-server`: servidor de descubrimiento.
- `catalogue-service`: gestion y busqueda de libros.
- `orders-service`: gestion de pedidos.
- `cloud-gateway`: punto de entrada y enrutamiento.
- `docker-compose.yml`: PostgreSQL para catalogue y orders.

## Arquitectura objetivo

```text
Frontend React
    |
    v
cloud-gateway
    |-- valida token opaco contra users-service
    |-- reenvia JWT interno como header accessToken
    |
    +--> catalogue-service
    +--> orders-service -- OrderCreated --> RabbitMQ --> comms-service --> SMTP/Gmail
    +--> users-service --> PostgreSQL users + Redis
    +--> comms-service --> WebSocket soporte + Gemini API

Todos los servicios deben registrarse en Eureka.
La comunicacion HTTP interna debe usar nombres Eureka, no localhost:puerto.
```

## Como se ejecutara localmente al final

Al finalizar la Actividad 3, el objetivo es ejecutar toda la arquitectura con:

```powershell
docker compose up --build
```

La ejecucion local final debera levantar:

- Eureka Server;
- Cloud Gateway;
- Catalogue Service;
- Orders Service;
- Users Service;
- Comms Service;
- PostgreSQL para catalogue;
- PostgreSQL para orders;
- PostgreSQL para users;
- Redis;
- RabbitMQ;
- frontend React si se integra dentro del Compose.

Las pruebas funcionales deberan hacerse siempre contra el Gateway, por ejemplo:

```powershell
curl.exe -i http://localhost:8080/api/catalogue/search
curl.exe -i -X POST http://localhost:8080/api/auth/login
curl.exe -i -X POST http://localhost:8080/api/orders/create -H "Authorization: Bearer TOKEN_OPACO"
```

## Servicios previstos

| Servicio | Estado inicial | Estado objetivo |
|---|---|---|
| `eureka-server` | Existente | Registrar todos los microservicios. |
| `cloud-gateway` | Existente | Defender endpoints, validar token opaco e inyectar `accessToken`. |
| `catalogue-service` | Existente | Mantener catalogo y busqueda; posible Elasticsearch opcional. |
| `orders-service` | Existente | Proteger pedidos, leer usuario desde JWT y emitir evento `OrderCreated`. |
| `users-service` | Pendiente | Registro/login, JWT, token opaco, Redis y validacion para Gateway. |
| `comms-service` | Pendiente | Consumir eventos, enviar correo, WebSocket y Gemini. |
| Frontend React | Pendiente/no localizado | Consumir siempre mediante API Gateway. |
| Redis | Pendiente | Almacenar token opaco -> JWT. |
| RabbitMQ | Pendiente | Gestionar eventos asincronos. |
| Elasticsearch | Opcional | Busqueda avanzada de catalogo. |

## Checklist de entrega

- [ ] Diagnostico inicial documentado.
- [ ] Matriz de cumplimiento creada.
- [ ] Plan por fases creado.
- [ ] Evidencia IA inicial creada.
- [ ] `users-service` implementado.
- [ ] PostgreSQL de usuarios configurado.
- [ ] Redis configurado.
- [ ] Login devuelve token opaco.
- [ ] Gateway valida token opaco contra `users-service`.
- [ ] Gateway responde 401 ante token ausente o invalido.
- [ ] Gateway reenvia JWT como header `accessToken`.
- [ ] Endpoints de datos de cliente protegidos.
- [ ] `orders-service` lee identidad desde `accessToken`.
- [ ] `orders-service` emite evento `OrderCreated`.
- [ ] RabbitMQ configurado.
- [ ] `comms-service` consume eventos.
- [ ] Envio SMTP/Gmail funcionando.
- [ ] WebSocket de soporte funcionando.
- [ ] Integracion Gemini funcionando.
- [ ] Todos los servicios registrados en Eureka.
- [ ] Comunicacion HTTP interna por nombres Eureka.
- [ ] Frontend consume exclusivamente mediante Gateway.
- [ ] Dockerfiles creados.
- [ ] Docker Compose local completo.
- [ ] Despliegue remoto publico preparado.
- [ ] Evidencia IA completada con prompts, resultados, lineas y tiempo.
- [ ] Video memoria grabado.
- [ ] Elasticsearch evaluado como extra opcional.
