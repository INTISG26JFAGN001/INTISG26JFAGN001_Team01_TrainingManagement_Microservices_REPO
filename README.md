# Training Management System (TMS)

## Overview

The **Training Management System** is a comprehensive microservices-based platform designed to manage training programs, assessments, evaluations, and course catalogs for organizations. Built with **Spring Boot** and **Spring Cloud**, the system leverages service discovery (Eureka), API Gateway routing, and centralized configuration management for scalability and maintainability.

---

## System Architecture

The system follows a **microservices architecture** with the following components:

### Infrastructure Services
- **Discovery Server (Eureka)** - Service registration and discovery
- **Config Server** - Centralized configuration management
- **API Gateway** - Single entry point for all client requests

### Business Logic Microservices
1. **Auth Manager** - User authentication and authorization
2. **Training Execution Service (TES)** - Batch, associate, and training schedule management
3. **Catalog Service (CAT)** - Technology, course, and stage management
4. **Assessment Service (ASM)** - Quiz and interview management
5. **Project Evaluation Service (PES)** - Project reviews and evaluations

---

## Microservices Details

### 1. Discovery Server (Eureka)
**Port:** `8761`

**Purpose:** Provides service registration and discovery for all microservices. All services register themselves with the Discovery Server at startup, allowing dynamic service discovery without hardcoded URLs.

**Key Features:**
- Automatic service registration
- Health check monitoring
- Service metadata management

---

### 2. Config Server
**Port:** `8888`

**Purpose:** Centralized configuration management for all microservices. Stores and manages environment-specific configurations (development, testing, production).

**Key Features:**
- Centralized configuration repository
- Environment-specific profiles
- Dynamic configuration updates

---

### 3. API Gateway
**Port:** `8000`

**Purpose:** Single entry point for all frontend requests. Routes requests to appropriate microservices and provides cross-cutting concerns like CORS handling.

**Base URL:** `http://localhost:8000`

**Key Features:**
- Request routing to microservices
- CORS configuration
- API aggregation

---

### 4. Auth Manager (Authentication & Authorization)
**Port:** `8080`

**Base URL:** `http://localhost:8080`

**Purpose:** Handles user authentication, authorization, and user management. Provides JWT token-based authentication for secure API access.

**Available Roles:**
- `ROLE_ADMIN` - Full system access
- `ROLE_TRAINER` - Training management and evaluation
- `ROLE_ASSOCIATE` - Training participant
- `ROLE_COACH` - Coaching and mentoring
- `ROLE_TECH_LEAD` - Technical oversight
- `ROLE_SCRUM_LEAD` - Scrum team leadership

#### API Endpoints

##### Authentication Endpoints
| Method | Endpoint | Description | Required Roles | Status Code |
|:-------|:---------|:------------|:---------------|:-----------|
| `POST` | `/auth/signup` | Register a new user | ADMIN | 201 Created |
| `POST` | `/auth/login` | User login with username and password | All | 202 Accepted |
| `POST` | `/auth/refresh-token` | Refresh access token using refresh token | All | 202 Accepted |

##### User Management Endpoints
| Method | Endpoint | Description | Required Roles | Status Code |
|:-------|:---------|:------------|:---------------|:-----------|
| `GET` | `/user/all` | Get all users | ADMIN | 200 OK |
| `GET` | `/user/{id}` | Get user by ID | ADMIN | 200 OK |
| `GET` | `/user/username?key={username}` | Get user by username | ADMIN | 200 OK |
| `GET` | `/user/email?key={email}` | Get user by email | ADMIN | 200 OK |
| `GET` | `/user/name?key={fullname}` | Search users by full name | ADMIN | 200 OK |
| `PUT` | `/user/update` | Update user details | ADMIN | 202 Accepted |
| `DELETE` | `/user/{id}` | Delete user by ID | ADMIN | 200 OK |

**Error Codes:**
- `U001` - User with ID does not exist
- `U002` - User with username already exists or invalid
- `U003` - User with email already exists or not found
- `A001` - Invalid password
- `T001` - Token invalid or expired
- `R001` - Role not found

---

### 5. Training Execution Service (TES)
**Port:** `8082`

**Base URL:** `http://localhost:8082`

**Purpose:** Manages training programs including batches, associates, enrollments, schedules, and trainers.

#### API Endpoints

##### Associate Management
| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `POST` | `/associates/create` | Create a new associate | ADMIN |
| `PUT` | `/associates/update` | Update associate details | ADMIN |
| `GET` | `/associates/{userId}` | Get associate by user ID | ADMIN, TRAINER, TECH_LEAD, COACH, ASSOCIATE |
| `GET` | `/associates` | Get all associates | ADMIN, TRAINER, TECH_LEAD, COACH, ASSOCIATE |
| `GET` | `/associates/batch?id={batchId}` | Get associates by batch | ADMIN, TRAINER, TECH_LEAD, COACH, ASSOCIATE |
| `GET` | `/associates/xp?value={xp}` | Get associates by experience points | ADMIN, TRAINER, TECH_LEAD, COACH, ASSOCIATE |

##### Batch Management
| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `POST` | `/batches` | Create a new batch | ADMIN |
| `GET` | `/batches` | Get all batches | ADMIN, TRAINER, TECH_LEAD, COACH |
| `GET` | `/batches/{id}` | Get batch by ID | ADMIN, TRAINER, TECH_LEAD, COACH |
| `DELETE` | `/batches/{id}` | Delete batch by ID | ADMIN |
| `PUT` | `/batches/{id}/status?status={BatchStatus}` | Update batch status | ADMIN, TRAINER |
| `GET` | `/batches/status?status={BatchStatus}` | Get batches by status | ADMIN, TRAINER, TECH_LEAD, COACH |
| `GET` | `/batches/course?course_id={courseId}` | Get batches by course ID | ADMIN, TRAINER, TECH_LEAD, COACH |
| `GET` | `/batches/trainer?trainer_id={trainerId}` | Get batches by trainer | ADMIN, TRAINER, TECH_LEAD, COACH |
| `GET` | `/batches/{id}/details` | Get detailed batch information | ADMIN, TRAINER |
| `GET` | `/batches/{batchId}/courses` | Get courses for a batch | ADMIN, TRAINER, TECH_LEAD, COACH, ASSOCIATE |

##### Enrollment Management
| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `POST` | `/enrollment` | Create new enrollment | ADMIN |
| `GET` | `/enrollment/{id}` | Get enrollment by ID | ADMIN, TRAINER, TECH_LEAD, COACH |
| `GET` | `/enrollment` | Get all enrollments | ADMIN, TRAINER, TECH_LEAD, COACH |
| `GET` | `/enrollment/batch?id={batchId}` | Get enrollments by batch | ADMIN, TRAINER, TECH_LEAD, COACH |
| `GET` | `/enrollment/associate?id={associateId}` | Get enrollments by associate | ADMIN, TRAINER, TECH_LEAD, COACH |
| `PUT` | `/enrollment/{id}/status?val={EnrollmentStatus}` | Update enrollment status | ADMIN |
| `GET` | `/enrollment/status?val={EnrollmentStatus}` | Get enrollments by status | ADMIN, TRAINER, TECH_LEAD, COACH |
| `DELETE` | `/enrollment/{id}` | Delete enrollment by ID | ADMIN |

##### Schedule Management
| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `POST` | `/schedule` | Create a new schedule | ADMIN |
| `GET` | `/schedule/{scheduleId}` | Get schedule by ID | ADMIN, TRAINER, TECH_LEAD, COACH |
| `GET` | `/schedule` | Get all schedules | ADMIN, TRAINER, TECH_LEAD, COACH |
| `PUT` | `/schedule/{scheduleId}/session-date?sessionDate={LocalDateTime}` | Update session date | ADMIN, TRAINER |
| `GET` | `/schedule/batch?id={batchId}` | Get schedules by batch | ADMIN, TRAINER, TECH_LEAD, COACH, ASSOCIATE |

##### Trainer Management
| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `POST` | `/trainer` | Add a new trainer | ADMIN |
| `GET` | `/trainer/{trainerId}` | Get trainer by ID | ADMIN, TRAINER, TECH_LEAD, COACH, ASSOCIATE |
| `DELETE` | `/trainer/{trainerId}` | Delete trainer by ID | ADMIN |
| `GET` | `/trainer` | Get all trainers | ADMIN, TRAINER, TECH_LEAD, COACH |
| `GET` | `/trainer/technology?technologyId={id}` | Get trainers by technology | ADMIN, TRAINER, TECH_LEAD, COACH |
| `PUT` | `/trainer/{trainerId}/technologies` | Update trainer's technologies | ADMIN |
| `GET` | `/trainer/{trainerId}/technologies` | Get trainer's technologies | ADMIN, TRAINER, TECH_LEAD, COACH |

---

### 6. Catalog Service (CAT)
**Port:** `8081`

**Base URL:** `http://localhost:8081`

**Purpose:** Manages the training catalog including technologies, courses, and training stages.

**Roles and Access:**
- `ADMIN` - Full CRUD access
- `TRAINER` - Create, Read, Update
- `TECH_LEAD` - Create, Read
- `COACH` - Read only
- `ASSOCIATE` - Read only

#### API Endpoints

##### Technology Management
| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `POST` | `/technologies` | Create technology | ADMIN, TECH_LEAD |
| `GET` | `/technologies` | Get all technologies | All Roles |
| `GET` | `/technologies/{id}` | Get technology by ID | All Roles |
| `PUT` | `/technologies/{id}` | Update technology | ADMIN, TRAINER, TECH_LEAD |
| `DELETE` | `/technologies` | Delete technology | ADMIN, TECH_LEAD |

##### Course Management
| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `POST` | `/courses` | Create course | ADMIN, TECH_LEAD |
| `GET` | `/courses` | Get all courses | All Roles |
| `GET` | `/courses/{id}` | Get course by ID | All Roles |
| `PUT` | `/courses/{id}` | Update course | ADMIN, TECH_LEAD, TRAINER |
| `DELETE` | `/courses` | Delete course | ADMIN, TECH_LEAD |

##### Stage Management
| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `POST` | `/stages` | Create stage | ADMIN, TECH_LEAD |
| `GET` | `/stages` | Get all stages | All Roles |
| `GET` | `/stages/{id}` | Get stage by ID | All Roles |
| `PUT` | `/stages/{id}` | Update stage | ADMIN, TECH_LEAD, TRAINER |
| `DELETE` | `/stages` | Delete stage | ADMIN, TECH_LEAD |

---

### 7. Assessment Service (ASM)
**Port:** `8084`

**Base URL:** `http://localhost:8084`

**Purpose:** Manages assessments including quizzes (MCQ with auto-grading) and interviews (manual grading). Integrates with PES for live score fetching.

#### API Endpoints

##### Core Assessment (General)
These endpoints work across both Quizzes and Interviews.

| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `GET` | `/assessments` | Get all assessments | ADMIN, TRAINER, TECH_LEAD, ASSOCIATE |
| `GET` | `/assessments/batch/{batchId}` | Get assessments by batch | ADMIN, TRAINER, TECH_LEAD, ASSOCIATE |
| `GET` | `/assessments/type/{type}` | Get assessments by type | ADMIN, TRAINER, TECH_LEAD, ASSOCIATE |
| `GET` | `/assessments/status/{status}` | Get assessments by status | ADMIN, TRAINER, TECH_LEAD |
| `GET` | `/assessments/batch/{batchId}/type/{type}` | Get assessments by batch and type | ADMIN, TRAINER, TECH_LEAD, ASSOCIATE |
| `GET` | `/assessments/batch/{batchId}/status/{status}` | Get assessments by batch and status | ADMIN, TRAINER, TECH_LEAD |
| `PATCH` | `/assessments/{assessmentId}` | Update assessment | ADMIN, TRAINER, TECH_LEAD |
| `DELETE` | `/assessments/{assessmentId}` | Delete assessment | ADMIN, TRAINER, TECH_LEAD |

##### Quiz Management (MCQ & Auto-Grading)
| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `POST` | `/assessments/quiz` | Create quiz | ADMIN, TRAINER, TECH_LEAD |
| `GET` | `/assessments/quiz/{quizId}` | Get quiz by ID | ADMIN, TRAINER, TECH_LEAD, ASSOCIATE |
| `GET` | `/assessments/quiz/batch/{batchId}` | Get quizzes by batch | ADMIN, TRAINER, TECH_LEAD, ASSOCIATE |
| `GET` | `/assessments/quiz/batch/{batchId}/status/{status}` | Get quizzes by batch and status | ADMIN, TRAINER, TECH_LEAD |
| `POST` | `/assessments/quiz/{quizId}/attempt` | Attempt quiz | ASSOCIATE |
| `GET` | `/assessments/quiz/{quizId}/attempts/{associateId}/result` | Get quiz attempt result | ADMIN, TRAINER, TECH_LEAD, ASSOCIATE |
| `GET` | `/assessments/quiz/{quizId}/attempts` | Get all quiz attempts | ADMIN, TRAINER, TECH_LEAD |

##### Interview Management (Manual Grading)
Handles interim and final evaluations. Scores are fetched live from PES service.

| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `POST` | `/assessments/interview` | Create interview | ADMIN, TRAINER, TECH_LEAD |
| `GET` | `/assessments/interview/{interviewId}` | Get interview by ID | ADMIN, TRAINER, TECH_LEAD, ASSOCIATE |
| `GET` | `/assessments/interview/batch/{batchId}` | Get interviews by batch | ADMIN, TRAINER, TECH_LEAD, ASSOCIATE |
| `GET` | `/assessments/interview/batch/{batchId}/category/{category}` | Get interviews by batch and category | ADMIN, TRAINER, TECH_LEAD, ASSOCIATE |
| `POST` | `/assessments/interview/{interviewId}/publish` | Publish interview | ADMIN, TRAINER, TECH_LEAD |
| `GET` | `/assessments/interview/{interviewId}/results` | Get interview results | ADMIN, TRAINER, TECH_LEAD |
| `GET` | `/assessments/interview/{interviewId}/associate/{associateId}` | Get interview for associate | ADMIN, TRAINER, TECH_LEAD, ASSOCIATE |

##### Rubric Management (Evaluation Logic)
Critical for interviews. **Weights must sum to exactly 100%**.

| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `POST` | `/assessments/{assessmentId}/rubrics` | Create rubric | ADMIN, TRAINER, TECH_LEAD |
| `GET` | `/assessments/{assessmentId}/rubrics` | Get rubrics | ADMIN, TRAINER, TECH_LEAD, ASSOCIATE |
| `GET` | `/assessments/{assessmentId}/rubrics/total-weight` | Get total rubric weight | ADMIN, TRAINER, TECH_LEAD |
| `DELETE` | `/assessments/{assessmentId}/rubrics/{rubricId}` | Delete rubric | ADMIN, TRAINER, TECH_LEAD |

---

### 8. Project Evaluation Service (PES)
**Port:** `8093`

**Base URL:** `http://localhost:8093`

**Purpose:** Manages project submissions, reviews, and evaluations. Provides evaluation scores to ASM service.

**Roles and Access:**
- `ADMIN` - Full CRUD access
- `TRAINER` - Create, Read, Update
- `TECH_LEAD` - Read
- `COACH` - Read
- `ASSOCIATE` - Read

#### API Endpoints

##### Project Management
| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `POST` | `/projects/submitProject` | Create/submit project | All Roles |
| `GET` | `/projects/getProjects` | Get all projects | All Roles |
| `GET` | `/projects/{projectId}` | Get project by ID | All Roles |
| `PUT` | `/projects/update/{projectId}` | Update project | All Roles |
| `DELETE` | `/projects/delete/{projectId}` | Delete project | All Roles |

##### Review Management
| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `POST` | `/reviews/project/{projectId}` | Create review | SCRUM_LEAD, TECH_LEAD |
| `GET` | `/reviews/project/{projectId}/all` | Get reviews by project | All Roles |
| `GET` | `/reviews/{reviewId}` | Get review by ID | ADMIN, TRAINER, TECH_LEAD, SCRUM_LEAD |
| `PUT` | `/reviews/{reviewId}` | Update review | SCRUM_LEAD, TECH_LEAD |
| `DELETE` | `/review/{reviewId}` | Delete review | ADMIN |

##### Evaluation Management
| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `GET` | `/evaluations/{evaluationId}` | Get evaluation by ID | ADMIN, TECH_LEAD, TRAINER |
| `POST` | `/evaluations/batch/{batchId}/calculate` | Create evaluation for batch | ADMIN, TECH_LEAD, TRAINER |
| `GET` | `/evaluations/batch/{batchId}/associate/{associateId}` | Get evaluation for associate | All Roles |

##### Interview Evaluation Management
| Method | Endpoint | Description | Authorized Roles |
|:-------|:---------|:------------|:-----------------|
| `POST` | `/interview-evaluations` | Submit interview evaluation | TRAINER |
| `GET` | `/interview-evaluations/assessment/{assessmentId}/associate/{associateId}` | Get evaluation | TRAINER, ASSOCIATE |
| `GET` | `/interview-evaluations/assessment/{assessmentId}` | Get all evaluations by assessment | TRAINER |
| `GET` | `/interview-evaluations/associate/{associateId}` | Get evaluations by associate | TRAINER, ASSOCIATE |

---

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher (for persistent storage)

### Build and Run

#### 1. Start Discovery Server (Eureka)
```bash
cd discovery-server
mvn spring-boot:run
```
Access at: `http://localhost:8761`

#### 2. Start Config Server
```bash
cd config-server
mvn spring-boot:run
```
Access at: `http://localhost:8888`

#### 3. Start all microservices
```bash
# Auth Manager
cd auth-manager
mvn spring-boot:run

# TES (Training Execution Service)
cd tes-service
mvn spring-boot:run

# CAT (Catalog Service)
cd cat-service
mvn spring-boot:run

# ASM (Assessment Service)
cd asm-service
mvn spring-boot:run

# PES (Project Evaluation Service)
cd pes-service
mvn spring-boot:run
```

#### 4. Start API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```
Access at: `http://localhost:8000`

---

## Authentication & Security

### JWT Token Flow
1. **Login** - Send username and password to `/auth/login`
2. **Receive Tokens** - Get `accessToken` in response and `refreshToken` in HTTP-only cookie
3. **Use Access Token** - Include in `Authorization: Bearer {accessToken}` header for API requests
4. **Refresh Token** - When access token expires, use `/auth/refresh-token` endpoint

### Default Admin Credentials
- **Username:** `admin`
- **Password:** `pass`

> **Note:** Change default credentials in production!

---

## Technology Stack

- **Framework:** Spring Boot 3.0+, Spring Cloud
- **Database:** MySQL
- **Authentication:** JWT (JSON Web Tokens)
- **Service Discovery:** Eureka
- **Configuration:** Spring Cloud Config
- **API Documentation:** Swagger/OpenAPI 3.0
- **Build Tool:** Maven

---

## API Documentation

Each microservice exposes Swagger UI documentation at:
```
http://{service-base-url}/swagger-ui.html
```

Examples:
- Auth Manager: `http://localhost:8080/swagger-ui.html`
- TES: `http://localhost:8082/swagger-ui.html`
- CAT: `http://localhost:8081/swagger-ui.html`
- ASM: `http://localhost:8084/swagger-ui.html`
- PES: `http://localhost:8093/swagger-ui.html`

---

## Error Handling

### Standard Error Response
```json
{
  "timestamp": "2026-04-16T15:00:00.000000",
  "message": "Error description",
  "errorCode": "ERROR_CODE",
  "path": "/endpoint/path"
}
```

### Common Error Codes

| Code | Description |
|:-----|:-----------|
| `U001` | User with ID does not exist |
| `U002` | User with username already exists |
| `U003` | User with email already exists |
| `A001` | Invalid password |
| `T001` | Token invalid or expired |
| `R001` | Role not found |
| `S500` | Internal server error |

---

## Project Structure

```
Training Management System/
├── discovery-server/          # Eureka service discovery
├── config-server/              # Centralized configuration
├── api-gateway/                # API Gateway (port 8000)
├── auth-manager/               # Auth service (port 8080)
├── tes-service/                # Training Execution Service (port 8082)
├── cat-service/                # Catalog Service (port 8081)
├── asm-service/                # Assessment Service (port 8084)
└── pes-service/                # Project Evaluation Service (port 8093)
```

---

## Contributing

1. Follow the existing code structure and naming conventions
2. Ensure all endpoints are documented in Swagger
3. Write unit tests for new functionality
4. Update this README for new endpoints or services

---

## License

This project is proprietary and confidential to Cognizant Technology Solutions.

---

## Support

For technical support or questions, please contact the development team.

---

**Last Updated:** April 16, 2026

