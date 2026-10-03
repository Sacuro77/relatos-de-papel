# Relatos de Papel — Actividad 2

Proyecto académico de **Desarrollo Web: Full Stack** orientado al desarrollo back-end con **Java, Spring Boot y arquitectura de microservicios**.

Esta rama contiene la entrega correspondiente a la **Actividad 2** del proyecto **Relatos de Papel**.

## Objetivo de la actividad

Construir la base back-end del sistema mediante microservicios independientes, descubrimiento de servicios, persistencia separada por dominio, API Gateway y comunicación entre servicios sin depender de direcciones `localhost` ni puertos fijos.

## Arquitectura

```text
Cliente
   |
   v
Cloud Gateway
   |
   +--> Catalogue Service ----> PostgreSQL catalogue_db
   |
   +--> Orders Service -------> PostgreSQL orders_db
              |
              +--> consulta Catalogue Service por nombre lógico

Eureka Server
   └── registro y descubrimiento de microservicios
```

## Componentes principales

| Componente | Función |
|---|---|
| `eureka-server` | Registro y descubrimiento de microservicios |
| `cloud-gateway` | Punto de entrada y enrutamiento de peticiones |
| `catalogue-service` | Gestión y consulta del catálogo de libros |
| `orders-service` | Gestión de órdenes de compra e ítems |
| PostgreSQL | Persistencia independiente para catálogo y órdenes |
| `docker-compose.yml` | Infraestructura local de bases de datos |

## Funcionalidades desarrolladas

- Arquitectura de microservicios con Spring Boot.
- Registro de servicios mediante Eureka.
- API Gateway para centralizar el acceso.
- Microservicio de catálogo con persistencia PostgreSQL.
- Microservicio de pedidos con persistencia PostgreSQL.
- Comunicación entre `orders-service` y `catalogue-service` mediante nombre lógico del servicio.
- Endpoints REST para catálogo y pedidos.
- Creación y consulta de órdenes a través del Gateway.
- Entidades JPA para libros, órdenes e ítems.
- Scripts DDL y DML para inicialización de las bases de datos.
- Catálogo de prueba con más de 100 libros.
- Evidencia documentada del uso de inteligencia artificial durante el desarrollo.

## Validaciones realizadas

Se validó manualmente:

- compilación de cada proyecto con Maven;
- ejecución local de Eureka Server;
- registro de `catalogue-service`, `orders-service` y `cloud-gateway` en Eureka;
- conexión independiente de cada microservicio con su base de datos;
- funcionamiento de endpoints REST de catálogo;
- funcionamiento de endpoints REST de pedidos;
- comunicación entre microservicios usando `catalogue-service` como nombre lógico;
- enrutamiento mediante Cloud Gateway;
- creación de órdenes mediante Gateway;
- consulta de órdenes recientes mediante Gateway.

## Persistencia

Los scripts SQL se encuentran en:

```text
sql/
```

La actividad incluye DDL y DML para las bases de datos de catálogo y pedidos, así como datos de prueba.

## Evidencia de uso de IA

La evidencia se encuentra en:

```text
evidencia-ia/
```

Incluye prompts utilizados, resultados obtenidos, correcciones manuales, estimaciones de líneas generadas con apoyo de IA y tiempo ahorrado.

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
