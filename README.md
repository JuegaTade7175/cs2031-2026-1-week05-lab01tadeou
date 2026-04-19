# Week 05 - Lab 01: Eventos y Asincronía en Spring Boot

Laboratorio del curso **CS2031** que demuestra el uso de **eventos de aplicación** y **procesamiento asíncrono** en Spring Boot mediante un sistema de órdenes con actualización automática de inventario.

---

## Objetivo

Entender cómo desacoplar lógica de negocio usando el sistema de eventos de Spring (`ApplicationEvent` / `@EventListener`) y cómo ejecutar tareas costosas en segundo plano con `@Async`, sin bloquear la respuesta HTTP al cliente.

---

## Conceptos clave

### 1. Eventos de aplicación (`ApplicationEvent`)

Spring ofrece un mecanismo de publicación/suscripción interno basado en el patrón **Observer**. Cuando ocurre algo relevante en la aplicación, un componente puede *publicar* un evento; otros componentes pueden *escucharlo* sin que el publicador los conozca directamente.

```
Publicador                   Bus de eventos de Spring         Listener
┌──────────────────┐         ┌─────────────────────┐         ┌──────────────────────┐
│ OrderDetailService│──────>│  ApplicationEvent    │──────> │InventoryUpdateNotifier│
│ publisher.       │         │  Publisher           │         │ @EventListener       │
│ publishEvent(e)  │         └─────────────────────┘         └──────────────────────┘
└──────────────────┘
```

**Clases involucradas:**

| Clase | Rol |
|---|---|
| `InventoryUpdateEvent` | Define el evento: qué datos se transportan (producto y cantidad) |
| `OrderDetailService` | Publicador: dispara el evento tras guardar un detalle de orden |
| `InventoryUpdateNotifier` | Listener: reacciona al evento reduciendo el stock |

**`InventoryUpdateEvent.java`** — extiende `ApplicationEvent` y lleva el contexto del evento:
```java
public class InventoryUpdateEvent extends ApplicationEvent {
    private final Product product;
    private final Integer quantity;

    public InventoryUpdateEvent(Object source, Product product, Integer quantity) {
        super(source);
        this.product = product;
        this.quantity = quantity;
    }
}
```

**`OrderDetailService.java`** — publica el evento al guardar:
```java
public void save(OrderDetail orderDetail) {
    repository.save(orderDetail);
    publisher.publishEvent(
        new InventoryUpdateEvent(this, orderDetail.getProduct(), orderDetail.getQuantity())
    );
}
```

**`InventoryUpdateNotifier.java`** — escucha y procesa:
```java
@EventListener
@Async
public void processInventoryUpdate(InventoryUpdateEvent event) {
    service.reduceStock(event.getProduct(), event.getQuantity());
}
```

---

### 2. Procesamiento asíncrono (`@Async`)

Por defecto, un listener de eventos se ejecuta **en el mismo hilo** que el publicador, bloqueando la respuesta HTTP mientras termina. Si la tarea es costosa (consulta lenta, llamada externa, etc.), esto degrada la experiencia del usuario.

Con `@Async`, Spring ejecuta el método en un **hilo separado** del pool de hilos, permitiendo que el publicador (y la respuesta HTTP) continúen de inmediato.

**Habilitación en la aplicación:**
```java
@EnableAsync          // activa el soporte de @Async
@SpringBootApplication
public class Week05Lab01Application { ... }
```

**Comportamiento simulado con proceso costoso:**

`InventoryService.reduceStock()` incluye un `Thread.sleep(60000L)` para simular una operación de 60 segundos (consulta lenta, integración externa, etc.). Con `@Async` en el listener, el endpoint `POST /orders/detail` responde de inmediato (HTTP 201) mientras el stock se actualiza en segundo plano.

```
Sin @Async (síncrono):
  POST /orders/detail ──> guarda orden ──> reduce stock (60s) ──> HTTP 201
                                           ^ cliente espera 60 segundos

Con @Async:
  POST /orders/detail ──> guarda orden ──> HTTP 201  (respuesta inmediata)
                                     └──> [hilo secundario] reduce stock (60s)
```

---

## Arquitectura del proyecto

El proyecto sigue una **arquitectura en capas** organizada por módulo de dominio:

```
src/main/java/org/week05lab01/
├── order/
│   ├── application/      OrderController.java
│   ├── domain/           Order.java, OrderService.java
│   └── infrastructure/   OrderRespository.java
├── orderDetail/
│   ├── application/      OrderDetailController.java
│   ├── domain/           OrderDetail.java, OrderDetailService.java  ← publica evento
│   └── infrastructure/   OrderDetailRepository.java
├── product/
│   ├── application/      ProductController.java
│   ├── domain/           Product.java, ProductService.java
│   └── infrastructure/   ProductRepository.java
├── inventory/
│   ├── application/      InventoryController.java
│   ├── domain/           Inventory.java, InventoryService.java      ← heavyProcess()
│   ├── events/           InventoryUpdateEvent.java                  ← define el evento
│   │                     InventoryUpdateNotifier.java               ← escucha el evento
│   └── infrastructure/   InventoryRepository.java
└── Week05Lab01Application.java                                      ← @EnableAsync
```

### Modelo de datos

```
Order (1) ──────< (N) OrderDetail (N) >────── (1) Product (1) ──── (1) Inventory
  - id                  - id                        - id                  - id
  - client              - quantity                  - name                - stock
                        - unitPrice                 - description
                                                    - price
```

---

## Endpoints REST

### Órdenes — `GET/POST /orders`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/orders` | Lista todas las órdenes |
| POST | `/orders` | Crea una nueva orden |

```json
// POST /orders
{ "client": "Juan Pérez" }
```

### Detalles de orden — `GET/POST /orders/detail`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/orders/detail` | Lista todos los detalles |
| POST | `/orders/detail` | Crea un detalle y **dispara el evento de inventario** |

```json
// POST /orders/detail
{
  "order": { "id": 1 },
  "product": { "id": 1 },
  "quantity": 3,
  "unitPrice": 25.99
}
```

### Productos — `GET/POST /products`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/products` | Lista todos los productos |
| POST | `/products` | Crea un nuevo producto |

```json
// POST /products
{ "name": "Laptop", "description": "Laptop gamer", "price": 2999.99 }
```

### Inventario — `GET/POST /inventory`, `GET /inventory/heavy`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/inventory` | Lista el inventario |
| POST | `/inventory` | Crea un registro de inventario |
| GET | `/inventory/heavy?ms={ms}` | Simula un proceso costoso de `ms` milisegundos (bloqueante) |

```json
// POST /inventory
{
  "product": { "id": 1 },
  "stock": 100
}
```

---

## Configuración y ejecución

### Prerrequisitos

- Java 21
- Maven 3.9+
- PostgreSQL

### Variables de entorno

Crea un archivo `.env` basado en `.env.example`:

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=week05_lab01
DB_USER=postgres
DB_PASSWORD=tu_password
```

### Ejecutar

```bash
# Con Maven Wrapper
./mvnw spring-boot:run

# O compilar y ejecutar el JAR
./mvnw package
java -jar target/week05-lab01-0.0.1-SNAPSHOT.jar
```

> La base de datos se crea automáticamente con `spring.jpa.hibernate.ddl-auto=create-drop`.

---

## Flujo completo de ejemplo

1. Crear un producto: `POST /products`
2. Crear una orden: `POST /orders`
3. Crear inventario inicial para el producto: `POST /inventory`
4. Crear un detalle de orden: `POST /orders/detail`
   - La respuesta HTTP llega **de inmediato** (201 Created)
   - En segundo plano, `InventoryUpdateNotifier` reduce el stock del producto

---

## Stack tecnológico

| Tecnología | Versión | Uso |
|---|---|---|
| Spring Boot | 3.2.5 | Framework principal |
| Spring Data JPA | — | Acceso a base de datos |
| Spring Events | — | Desacoplamiento con eventos |
| `@Async` / `@EnableAsync` | — | Procesamiento asíncrono |
| PostgreSQL | 42.7.3 | Base de datos relacional |
| Lombok | 1.18.40 | Reducción de boilerplate |
| Java | 21 | Lenguaje |
