# Fase 6 - WebSocket Chat de Soporte

Proyecto: Relatos de Papel  
Servicio: `comms-service`

## Objetivo

Agregar a `comms-service` un endpoint WebSocket simple para chat de soporte, manteniendo funcionando el consumo RabbitMQ y el email mock/log de la Fase 5.

En esta fase no se integra Gemini. La respuesta del chat es local y sirve como base para la Fase 7.

## Endpoint WebSocket

```text
ws://localhost:8084/ws/support
```

Clase principal:

```text
comms-service/src/main/java/com/actividad3/comms_service/websocket/SupportChatEndpoint.java
```

Configuracion:

```text
comms-service/src/main/java/com/actividad3/comms_service/config/WebSocketConfig.java
```

## Flujo implementado

1. El cliente abre una conexion WebSocket contra `/ws/support`.
2. `comms-service` registra en log la conexion abierta.
3. El cliente envia un mensaje de texto.
4. `comms-service` registra el mensaje recibido.
5. `comms-service` responde con un mensaje mock:

```text
Hola, soy el asistente de Relatos de Papel. Recibi tu mensaje: <mensaje>
```

6. `comms-service` registra la respuesta enviada.
7. Al cerrar la conexion, `comms-service` registra el cierre.

## Implementacion tecnica

La implementacion usa Jakarta WebSocket sobre el servidor embebido Tomcat de `spring-boot-starter-webmvc`.

Durante la fase se intento agregar dependencias especificas de WebSocket, pero Maven no pudo descargar nuevos artefactos por error de certificado PKIX contra Maven Central. Para mantener la fase compilable y defendible, se uso la API Jakarta WebSocket disponible por el stack actual.

No se modifico `cloud-gateway` en esta fase. La prueba directa se realiza contra `comms-service` en `localhost:8084`. Si el frontend debe consumir tambien WebSocket mediante Gateway, se dejara como ajuste de integracion posterior.

## Comandos de prueba

Compilar solo `comms-service`:

```powershell
Push-Location comms-service
.\mvnw.cmd compile
Pop-Location
```

Levantar `comms-service`:

```powershell
Push-Location comms-service
.\mvnw.cmd spring-boot:run
Pop-Location
```

Probar desde navegador:

```javascript
const ws = new WebSocket("ws://localhost:8084/ws/support");
ws.onopen = () => ws.send("Hola, necesito ayuda con mi pedido");
ws.onmessage = (event) => console.log("Respuesta:", event.data);
ws.onclose = (event) => console.log("Cerrado:", event.code, event.reason);
ws.onerror = (event) => console.error("Error WebSocket:", event);
```

Alternativas:

```powershell
npx wscat -c ws://localhost:8084/ws/support
```

Tambien se puede probar con:

- Postman WebSocket;
- extension WebSocket de VS Code;
- consola del navegador.

## Logs esperados

Al arrancar:

```text
Endpoint WebSocket registrado path=/ws/support
```

Al conectar:

```text
WebSocket soporte conexion abierta sessionId=...
```

Al enviar mensaje:

```text
WebSocket soporte mensaje recibido sessionId=... payload=Hola, necesito ayuda con mi pedido
WebSocket soporte respuesta enviada sessionId=... payload=Hola, soy el asistente de Relatos de Papel. Recibi tu mensaje: Hola, necesito ayuda con mi pedido
```

Al cerrar:

```text
WebSocket soporte conexion cerrada sessionId=... reason=...
```

## Evidencia para video

- Mostrar `comms-service` arrancando en puerto `8084`.
- Mostrar el log de registro del endpoint `/ws/support`.
- Abrir cliente WebSocket con `ws://localhost:8084/ws/support`.
- Enviar el mensaje `Hola, necesito ayuda con mi pedido`.
- Mostrar la respuesta mock.
- Mostrar logs de conexion, mensaje recibido, respuesta enviada y cierre.
- Indicar que Gemini se integrara en Fase 7.

## Criterio cubierto

Esta fase cubre el Criterio 3:

```text
Asincronia con WebSockets - 1 punto
```

Estado:

```text
Implementado de forma basica/mock en comms-service. Pendiente integracion Gemini en Fase 7.
```

## Riesgos y pendientes

- El chat responde de forma local/mock; Gemini queda pendiente para Fase 7.
- El endpoint todavia se prueba directamente contra `comms-service`; exponerlo via Gateway queda pendiente si se requiere para el frontend final.
- No hay autenticacion en el WebSocket en esta fase. Si el chat debe identificar usuario, se podra validar token opaco o JWT interno en una fase posterior.
