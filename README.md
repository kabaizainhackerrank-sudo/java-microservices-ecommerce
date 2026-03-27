🛒 Java Microservices E-Commerce Platform
> Plataforma de e-commerce construida con arquitectura de microservicios, orientada a eventos, como proyecto de aprendizaje y práctica profesional.
---
📌 Descripción
Este proyecto simula el ciclo completo de una compra en línea: desde el registro del usuario hasta la aprobación o rechazo de una orden por un motor de riesgo/fraude.
Está compuesto por 8 microservicios independientes, cada uno con su propia base de datos y responsabilidad de dominio. La comunicación entre servicios se realiza mediante eventos asincrónicos con RabbitMQ, evitando el acoplamiento directo entre ellos.
---
🏗️ Arquitectura
```
┌─────────────────────────────────────────────────────────────┐
│                        API Gateway                          │
└───────────────────────────┬─────────────────────────────────┘
                            │ JWT Auth
        ┌───────────────────┼───────────────────┐
        ▼                   ▼                   ▼
┌──────────────┐   ┌──────────────┐   ┌──────────────────┐
│ auth-service │   │customer-serv.│   │  catalog-service │
│   (Java)     │   │   (Java)     │   │    (Kotlin)      │
└──────┬───────┘   └──────────────┘   └──────────────────┘
       │
       │ RabbitMQ Events
       ├──── user.created ──► customer-service
       └──── user.created ──► notification-service
```
Patrón: Event-Driven Architecture  
Mensajería: RabbitMQ (exchanges + múltiples colas)  
Seguridad: JWT con roles por servicio
---
📦 Servicios
✅ Implementados
Servicio	Lenguaje	Responsabilidad
`auth-service`	Java	Registro, login, JWT, roles
`customer-service`	Java	Perfil, direcciones, preferencias, estado
`catalog-service`	Kotlin	Productos, categorías, stock, búsquedas
🚧 En desarrollo
Servicio	Responsabilidad
`order-service`	Creación y gestión de órdenes
`payment-service`	Procesamiento de pagos
`risk-service`	Motor de detección de fraude
`notification-service`	Emails y notificaciones
`api-gateway`	Enrutamiento y autenticación centralizada
---
⚙️ Stack Tecnológico
Tecnología	Uso
Java 17	auth-service, customer-service
Kotlin	catalog-service
Spring Boot 3	Framework principal
Spring Security	Autenticación y autorización
PostgreSQL	Base de datos por servicio
Supabase	PostgreSQL cloud
RabbitMQ	Mensajería asíncrona entre servicios
CloudAMQP	RabbitMQ cloud
JWT	Tokens de acceso
---
🔐 Roles del sistema
Rol	Descripción
`CUSTOMER`	Usuario final que compra
`ADMIN`	Administrador de la plataforma
`RISK_ANALYST`	Analista del motor de fraude
`SUPPORT`	Soporte al cliente
---
🚀 Cómo ejecutar
> Cada servicio es independiente y puede levantarse por separado.
Requisitos
Java 17+
Kotlin 1.9+
Docker (opcional)
Cuenta en Supabase (PostgreSQL)
Cuenta en CloudAMQP (RabbitMQ)
Variables de entorno
Cada servicio tiene su propio `application.yml`. Configurar las siguientes variables:
```env
# Base de datos
DB_URL=jdbc:postgresql://<host>/<database>
DB_USERNAME=<usuario>
DB_PASSWORD=<contraseña>

# RabbitMQ
RABBITMQ_HOST=<host>
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=<usuario>
RABBITMQ_PASSWORD=<contraseña>

# JWT
JWT_SECRET=<clave-secreta>
JWT_EXPIRATION=86400000
```
Ejecutar un servicio
```bash
cd services/auth-service
./mvnw spring-boot:run
```
---
📡 Eventos RabbitMQ
Exchanges (los "carteros")
Exchange	Servicio dueño	Descripción
`auth.events`	`auth-service`	Eventos de autenticación y registro
`customer.events`	`customer-service`	Eventos del perfil del cliente
`catalog.events`	`catalog-service`	Eventos de productos y stock
`order.events`	`order-service` (futuro)	Eventos de órdenes
Eventos publicados
Evento (routing key)	Exchange	Publicado por	Consumido por
`user.registered`	`auth.events`	`auth-service`	`customer-service`, `notification-service`
`customer.updated`	`customer.events`	`customer-service`	(otros servicios)
`customer.status.changed`	`customer.events`	`customer-service`	(otros servicios)
`product.created`	`catalog.events`	`catalog-service`	(otros servicios)
`product.deactivated`	`catalog.events`	`catalog-service`	(otros servicios)
`stock.updated`	`catalog.events`	`catalog-service`	(otros servicios)
Eventos consumidos
Cola (buzón)	Evento	Consumido por	Acción
`user.created.queue`	`user.registered`	`customer-service`	Crea el perfil del cliente
`customer.updated.queue`	`customer.updated`	`customer-service`	Actualiza datos del cliente
`customer.status.changed.queue`	`customer.status.changed`	`customer-service`	Cambia estado del cliente
`order.confirmed.queue`	`order.confirmed`	`catalog-service`	Descuenta stock
`order.cancelled.queue`	`order.cancelled`	`catalog-service`	Devuelve stock
---
🎯 Objetivo del proyecto
Este proyecto nació con un propósito claro: construir algo real mientras buscaba trabajo como desarrollador backend.
No espero que esté perfecto. Lo muestro mientras lo construyo, porque el proceso dice más de un dev que el resultado final.
---
👨‍💻 Autor
kabaizainhackerrank-sudo  
LinkedIn · GitHub
---
> ⭐ Si este proyecto te resulta útil o interesante, dejá una estrella. Ayuda a que más personas lo encuentren.
