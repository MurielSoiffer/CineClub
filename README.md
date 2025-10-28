
# 🎬 CineClub Backend API


## 🌟 Resumen del Proyecto

<img align="right" width=300px alt="cine" src="https://raw.githubusercontent.com/MurielSoiffer/MurielSoiffer/master/assets/cine.gif"/>


Este repositorio contiene la implementación de una **API REST de backend** para el sistema de gestión de un cine.  
El enfoque principal es la administración de la cartelera, la disponibilidad de asientos y el **ciclo de vida de tickets**, incluyendo la funcionalidad de **reserva temporal (hold) con TTL** y la adquisición final mediante una transición explícita de estados.

El proyecto está desarrollado en **Java/Spring Boot** con base de datos **PostgreSQL**, siguiendo una arquitectura REST y diseñado para ejecutarse en contenedores **Docker**.

---

## 🎯 Objetivos Funcionales Clave

El servicio cumple con los siguientes requerimientos:

- **Gestión de Catálogo (ADMIN):** CRUD completo para **Películas**, **Salas** (con butacas) y **Funciones**.  
  - Validación de funciones para evitar solapamientos en una misma sala.
- **Reservas Temporales (Hold):** Bloqueo de 1 a N butacas con un **TTL configurable** y liberación automática al vencer.
- **Ciclo de Vida del Ticket:** Transición entre estados para representar reserva, compra y cancelación.
- **Adquisición de Tickets:** Confirmación de compra mediante `POST /api/tickets/{ticketId}/confirm`, solo por el propietario.
- **Integración con Pasarela de Pagos:** Creación de *payment intents* y confirmación vía **callbacks asíncronos** (`success` / `fail`), con idempotencia garantizada.

---

## 🛡️ Seguridad y Autorización

- **Autenticación:** Basada en **JWT**, con almacenamiento seguro de secretos en variables de entorno.  
- **Autorización por Roles:**
  - **ADMIN:** CRUD de catálogo completo y operaciones administrativas.  
  - **USER:** Creación y cancelación de holds, confirmación de tickets y consulta de disponibilidad.  
- **Propiedad de Recursos:** Los usuarios solo pueden operar sobre sus propios tickets. 

---

## 🛠️ Stack Tecnológico y Arquitectura

| Componente | Tecnología | Descripción |
| :--- | :--- | :--- |
| **Lenguaje** | Java (LTS) | Lenguaje principal del backend. |
| **Framework** | Spring Boot | Incluye Spring Web, Data JPA, Security y Validation. |
| **Base de Datos** | PostgreSQL | Persistencia relacional de entidades. |
| **ORM y Migraciones** | Hibernate + Flyway / Liquibase | Gestión de esquema y migraciones. |
| **Documentación** | Springdoc / OpenAPI / Swagger | Documentación automática de la API. |
| **Testing** | JUnit 5, Mockito, MockMvc / RestAssured | Pruebas unitarias e integrales. |
| **Contenedores** | Docker / Docker Compose | Despliegue y entorno reproducible. |

---

## ⚙️ Configuraciones y Parámetros

Parámetros configurables por variables de entorno o archivos `.env`:

| Parámetro | Valor por Defecto | Descripción |
| :--- | :--- | :--- |
| `HOLD_TTL_DEFAULT` | `300s` | Tiempo de vida por defecto de las reservas. |
| `HOLD_TTL_MIN` / `HOLD_TTL_MAX` | `60s – 900s` | Rango permitido del TTL. |
| `PRE_SCREENING_THRESHOLD` | `10m` | Umbral mínimo antes del inicio de función para permitir holds. |
| `USER_HOLD_LIMIT` | `6` | Máximo de asientos en hold simultáneos por usuario. |
| `PAYMENT_SECRET` | `—` | Secreto para validar firma/HMAC de callbacks. |

---

## 🚀 Despliegue y Ejecución Local

### 🧩 Requisitos Previos
- **Docker**  
- **Docker Compose**

### ▶️ Pasos de ejecución

```bash
# 1. Clonar el repositorio
git clone https://github.com/<usuario>/cineclub-backend.git
cd cineclub-backend

# 2. Levantar los contenedores (la app se construirá automáticamente)
docker-compose up
```

La API quedará disponible en:  
👉 `http://localhost:8080`

Documentación Swagger en:  
📘 `http://localhost:8080/swagger-ui.html`

---

## 🧪 Pruebas

Las pruebas unitarias y de integración se ejecutan automáticamente durante el build del contenedor.  
También podés correrlas manualmente en tu entorno local con:

```bash
docker exec -it cineclub-backend mvn test
```

Incluye:
- Pruebas unitarias de servicios y reglas de negocio (TTL, límites, validaciones).  
- Pruebas de integración REST (hold → confirmación → expiración).  
- Pruebas de seguridad (roles y propiedad de recursos).  


---

## 📊 Endpoints Principales

| Recurso | Método | Endpoint | Rol | Descripción |
| :--- | :--- | :--- | :--- | :--- |
| **Películas** | GET | `/api/movies` | Público | Listar y buscar películas |
| **Películas** | POST | `/api/movies` | ADMIN | Crear una nueva película |
| **Salas** | GET | `/api/rooms` | ADMIN | Listar salas |
| **Funciones** | GET | `/api/screenings` | Público | Ver funciones disponibles |
| **Asientos** | GET | `/api/screenings/{id}/seats` | USER | Consultar disponibilidad |
| **Holds** | POST | `/api/screenings/{id}/holds` | USER | Crear reserva temporal |
| **Tickets** | POST | `/api/tickets/{ticketId}/confirm` | USER | Confirmar compra |
| **Tickets** | DELETE | `/api/tickets/{ticketId}` | USER | Cancelar hold |
| **Tickets** | GET | `/api/me/tickets` | USER | Ver tickets del usuario |

---

