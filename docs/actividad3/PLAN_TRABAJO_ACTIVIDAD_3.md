# Plan de Trabajo - Actividad 3

Proyecto: Relatos de Papel

## Fase 0: diagnostico y evidencia base

Objetivo:

- Documentar el estado inicial del proyecto, riesgos, matriz de cumplimiento y evidencia IA inicial.

Archivos previstos:

- `docs/actividad3/DIAGNOSTICO_INICIAL.md`
- `docs/actividad3/MATRIZ_CUMPLIMIENTO_ACTIVIDAD_3.md`
- `docs/actividad3/PLAN_TRABAJO_ACTIVIDAD_3.md`
- `evidencia-ia/actividad-3-prompts.md`
- `evidencia-ia/actividad-3-metricas.md`
- `README_ACTIVIDAD_3.md`

Prueba esperada:

- Verificar que los archivos existen y que `git status` muestra solo documentacion nueva.

Evidencia para video:

- Mostrar documentacion inicial, matriz y plan.

Criterio cubierto:

- Criterio 6 parcialmente, preparacion de evidencia.

Riesgo:

- Bajo. No se modifica codigo fuente ni configuracion.

## Fase 1: users-service con PostgreSQL, Redis, JWT y token opaco

Objetivo:

- Crear microservicio de usuarios con registro/login, JWT interno y token opaco publico almacenado en Redis.

Archivos previstos:

- `users-service/pom.xml`
- `users-service/src/main/java/.../controller/AuthController.java`
- `users-service/src/main/java/.../service/AuthService.java`
- `users-service/src/main/java/.../service/JwtService.java`
- `users-service/src/main/java/.../service/OpaqueTokenService.java`
- `users-service/src/main/java/.../entity/User.java`
- `users-service/src/main/java/.../repository/UserRepository.java`
- `users-service/src/main/java/.../dto/*.java`
- `users-service/src/main/resources/application.properties`

Prueba esperada:

```powershell
curl.exe -i -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"email\":\"test@test.com\",\"password\":\"123456\"}"
```

Evidencia para video:

- Login devuelve token opaco.
- Redis contiene relacion token opaco -> JWT.
- `users-service` registrado en Eureka.

Criterio cubierto:

- Criterio 1.

Riesgo:

- Medio. Requiere diseno correcto de expiracion, secretos JWT y contratos para Gateway.

## Fase 2: filtro Java en Gateway

Objetivo:

- Implementar filtro Java formal en Gateway para detectar rutas publicas/protegidas, validar token opaco contra `users-service` y reenviar `accessToken`.

Archivos previstos:

- `cloud-gateway/src/main/java/.../config/GatewayRoutesConfig.java`
- `cloud-gateway/src/main/java/.../filter/PhantomTokenAuthFilter.java`
- `cloud-gateway/src/main/java/.../client/UsersAuthClient.java`
- `cloud-gateway/src/main/java/.../config/SecurityRouteProperties.java`

Prueba esperada:

```powershell
curl.exe -i http://localhost:8080/api/orders/recent
curl.exe -i http://localhost:8080/api/orders/recent -H "Authorization: Bearer TOKEN_OPACO"
```

Evidencia para video:

- Sin token devuelve 401.
- Con token valido reenvia al servicio.
- Logs del Gateway muestran validacion.

Criterio cubierto:

- Criterio 1.

Riesgo:

- Alto. Gateway es WebMVC, por lo que el filtro debe usar APIs compatibles con Spring Cloud Gateway Server WebMVC.

## Fase 3: proteger orders-service y leer accessToken

Objetivo:

- Evitar que el cliente envie `userId` arbitrario y derivar identidad desde el JWT recibido en header `accessToken`.

Archivos previstos:

- `orders-service/src/main/java/.../controller/OrderController.java`
- `orders-service/src/main/java/.../service/OrderService.java`
- `orders-service/src/main/java/.../security/JwtClaimsExtractor.java`
- DTOs de orders si se elimina `userId` del request.

Prueba esperada:

```powershell
curl.exe -i -X POST http://localhost:8080/api/orders/create -H "Authorization: Bearer TOKEN_OPACO" -H "Content-Type: application/json" -d "{\"items\":[{\"bookId\":1,\"quantity\":1}]}"
```

Evidencia para video:

- La orden queda asociada al usuario del JWT.
- Intento sin token falla en Gateway.

Criterio cubierto:

- Criterio 1.

Riesgo:

- Medio. Hay que mantener compatibilidad con el modelo actual y evitar exponer datos de otros usuarios.

## Fase 4: RabbitMQ y evento OrderCreated

Objetivo:

- Emitir evento `OrderCreated` desde `orders-service` cuando una compra se registra correctamente.

Archivos previstos:

- `orders-service/src/main/java/.../event/OrderCreatedEvent.java`
- `orders-service/src/main/java/.../messaging/OrderEventPublisher.java`
- `orders-service/src/main/java/.../config/RabbitConfig.java`
- propiedades RabbitMQ.

Prueba esperada:

```powershell
curl.exe -i -X POST http://localhost:8080/api/orders/create -H "Authorization: Bearer TOKEN_OPACO" -H "Content-Type: application/json" -d "{\"items\":[{\"bookId\":1,\"quantity\":1}]}"
```

Evidencia para video:

- RabbitMQ Management muestra exchange/queue.
- Logs de publicacion del evento.

Criterio cubierto:

- Criterio 2.

Riesgo:

- Medio. Debe publicarse solo despues de guardar correctamente la orden.

## Fase 5: comms-service consumidor + SMTP/Gmail

Objetivo:

- Crear `comms-service` para consumir eventos RabbitMQ y enviar correo de confirmacion con SMTP/Gmail.

Archivos previstos:

- `comms-service/pom.xml`
- `comms-service/src/main/java/.../listener/OrderCreatedListener.java`
- `comms-service/src/main/java/.../service/EmailService.java`
- `comms-service/src/main/java/.../config/RabbitConfig.java`
- `comms-service/src/main/resources/application.properties`

Prueba esperada:

```powershell
curl.exe -i -X POST http://localhost:8080/api/orders/create -H "Authorization: Bearer TOKEN_OPACO" -H "Content-Type: application/json" -d "{\"items\":[{\"bookId\":1,\"quantity\":1}]}"
```

Evidencia para video:

- Log de evento consumido.
- Correo recibido en Gmail.

Criterio cubierto:

- Criterio 2.

Riesgo:

- Medio/alto. Gmail requiere contrasena de aplicacion y manejo seguro de variables.

## Fase 6: WebSocket

Objetivo:

- Exponer chat de soporte en `comms-service` mediante WebSocket.

Archivos previstos:

- `comms-service/src/main/java/.../config/WebSocketConfig.java`
- `comms-service/src/main/java/.../controller/SupportChatController.java`
- `comms-service/src/main/java/.../dto/ChatMessage.java`

Prueba esperada:

- Conectar cliente WebSocket a endpoint de soporte y enviar mensaje.

Evidencia para video:

- Chat funcionando en cliente o herramienta WebSocket.

Criterio cubierto:

- Criterio 3.

Riesgo:

- Alto. Requiere decidir si el WebSocket se expone directo o a traves del Gateway.

## Fase 7: Gemini

Objetivo:

- Integrar Gemini API para responder mensajes de soporte.

Archivos previstos:

- `comms-service/src/main/java/.../client/GeminiClient.java`
- `comms-service/src/main/java/.../service/GeminiSupportService.java`
- propiedades de API key.

Prueba esperada:

- Enviar pregunta por chat y recibir respuesta generada por Gemini.

Evidencia para video:

- Conversacion de soporte con respuesta IA.

Criterio cubierto:

- Refuerzo de Criterio 3 y requisito especifico de integracion Gemini.

Riesgo:

- Medio. Depende de API key, cuota y manejo de errores externos.

## Fase 8: Docker Compose completo

Objetivo:

- Ejecutar localmente toda la arquitectura con Docker Compose.

Archivos previstos:

- `docker-compose.yml`
- Dockerfile por microservicio.
- `.env.example`
- perfiles/variables de entorno.

Prueba esperada:

```powershell
docker compose up --build
```

Evidencia para video:

- Todos los servicios registrados en Eureka.
- Gateway responde.
- Redis y RabbitMQ activos.

Criterio cubierto:

- Criterio 4.

Riesgo:

- Alto. Requiere parametrizar `localhost`, puertos, dependencias y orden de arranque.

## Fase 9: documentacion IA

Objetivo:

- Completar evidencia obligatoria del uso de IA en Actividad 3.

Archivos previstos:

- `evidencia-ia/actividad-3-prompts.md`
- `evidencia-ia/actividad-3-metricas.md`
- posibles capturas o anexos.

Prueba esperada:

- Revisar que cada fase tenga prompt, resultado, decision, lineas aproximadas y correcciones manuales.

Evidencia para video:

- Mostrar documentos de IA y explicar validacion manual.

Criterio cubierto:

- Criterio 6.

Riesgo:

- Bajo. El riesgo es olvidar actualizar la evidencia durante el desarrollo.

## Fase 10: despliegue remoto Railway/Vercel

Objetivo:

- Publicar backend en Railway y frontend en Vercel si aplica.

Archivos previstos:

- documentacion de despliegue;
- variables Railway;
- configuracion Docker/servicios;
- README actualizado.

Prueba esperada:

```powershell
curl.exe -i https://URL-GATEWAY-REMOTA/actuator/health
```

Evidencia para video:

- URLs publicas funcionando.
- Prueba de login y pedido en remoto.

Criterio cubierto:

- Criterio 5.

Riesgo:

- Alto. Eureka, Gateway y servicios multiples en Railway pueden requerir ajustes de red y variables.

## Fase 11: video memoria

Objetivo:

- Preparar y grabar la demostracion final.

Archivos previstos:

- guion/checklist de video;
- capturas;
- comandos finales.

Prueba esperada:

- Recorrido completo sin errores: login, Gateway, pedido, evento, correo, chat, Gemini, Docker y remoto.

Evidencia para video:

- Video final entregable.

Criterio cubierto:

- Criterio 7.

Riesgo:

- Bajo/medio. Depende de que las fases tecnicas esten estables.

## Fase 12 opcional: Elasticsearch

Objetivo:

- Agregar busqueda avanzada de catalogo con Elasticsearch para puntos extra.

Archivos previstos:

- configuracion Elasticsearch en Compose;
- dependencias catalogue;
- documento/index de libros;
- servicio de indexacion y busqueda.

Prueba esperada:

```powershell
curl.exe -i "http://localhost:8080/api/catalogue/search?q=borges"
```

Evidencia para video:

- Busqueda textual avanzada mostrando resultados relevantes.

Criterio cubierto:

- Extra hasta 2 puntos sin superar 10.

Riesgo:

- Medio. No debe abordarse hasta cerrar los criterios obligatorios.
