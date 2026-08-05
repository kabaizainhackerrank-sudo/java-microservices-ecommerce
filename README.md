# Java Microservices E-Commerce Platform

> Production-oriented e-commerce backend platform built with Java, Kotlin, Spring Boot and an Event-Driven Microservices Architecture.

This project simulates the complete lifecycle of an e-commerce purchase, from user registration to order approval or rejection through a fraud/risk engine.

The system is composed of independent microservices, each owning its own business domain and database. Services communicate asynchronously using RabbitMQ, following an Event-Driven Architecture that promotes loose coupling, scalability and maintainability.

---

# Architecture

```text
                               +----------------------+
                               |     API Gateway      |
                               +----------------------+
                                          |
                                 JWT Authentication
                                          |
      ----------------------------------------------------------------
      |                     |                     |                   |
+---------------+    +---------------+    +---------------+    +---------------+
| Auth Service  |    | Customer      |    | Catalog       |    | Order Service |
| Java          |    | Java          |    | Kotlin        |    | Java          |
+---------------+    +---------------+    +---------------+    +---------------+
        |                     |                     |                  |
        +--------------------------------------------------------------+
                               RabbitMQ Events
                                      |
                        Event-Driven Communication
```

---

# Architecture Highlights

| Feature | Implementation |
|----------|----------------|
| Architecture Style | Event-Driven Microservices |
| Communication | RabbitMQ |
| Authentication | JWT |
| API Gateway | Spring Cloud Gateway |
| Databases | Database per Service |
| Backend Languages | Java & Kotlin |
| Containerization | Docker |
| Build Tool | Maven / Gradle |

---

# Project Goals

The objective of this project is to demonstrate enterprise backend development practices including:

- Event-Driven Architecture
- Microservices
- Domain Separation
- Asynchronous Messaging
- JWT Authentication
- API Gateway
- Database per Service
- Containerized Deployment
- Clean and Maintainable Backend Design

---

# Event Flow

Example:

User Registration

```
User registers
        │
        ▼
Auth Service
        │
 Publishes "user.created"
        │
        ▼
RabbitMQ
        │
        ├────────────► Customer Service
        │
        └────────────► Notification Service
```

Every service reacts independently without direct coupling.

---

# Implemented Services

| Service | Language | Responsibility |
|----------|----------|----------------|
| Auth Service | Java | Authentication and JWT generation |
| Customer Service | Java | Customer management |
| Catalog Service | Kotlin | Product catalog |

---

# Planned Services

- Order Service
- Inventory Service
- Payment Service
- Fraud Detection Service
- Notification Service

---

# Technology Stack

## Backend

- Java
- Kotlin
- Spring Boot

## Messaging

- RabbitMQ

## Security

- Spring Security
- JWT

## Database

- PostgreSQL

## Infrastructure

- Docker
- Docker Compose

---

# Running the Project

```bash
git clone ...

docker compose up

mvn clean install

mvn spring-boot:run
```

---

# Future Improvements

- Distributed Tracing
- Observability
- Resilience4J
- Kubernetes Deployment
- Contract Testing
- CI/CD Pipeline
- Monitoring with Prometheus & Grafana

---

# Author

Backend Engineering Portfolio Project

Designed and implemented as a hands-on project to demonstrate modern enterprise backend development using Java, Kotlin and Spring Boot.
