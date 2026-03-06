# 🎓 College ERP Backend

![Java](https://img.shields.io/badge/Java-17-orange?logo=java)
![Spring Boot](https://img.shields.io/badge/SpringBoot-Backend-brightgreen?logo=springboot)
![Spring Security](https://img.shields.io/badge/Security-JWT-red)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue?logo=postgresql)
![Docker](https://img.shields.io/badge/Container-Docker-blue?logo=docker)
![API](https://img.shields.io/badge/API-REST-green)
![License](https://img.shields.io/badge/License-MIT-yellow)

A **Spring Boot based RESTful backend system** for managing college operations such as students, teachers, subjects, authentication, and departments.
This project follows **modern backend development practices** including **JWT authentication, layered architecture, and Docker containerization**.

---

# 🚀 Features

✔ Secure **JWT Authentication & Authorization**
✔ **Role Based Access Control** (Admin / Teacher / Student)
✔ **Student Management System**
✔ **Teacher Management System**
✔ **Subject & Department Management**
✔ Clean **RESTful API design**
✔ **Dockerized backend deployment**
✔ **Swagger API Documentation**
✔ **Layered Architecture (Controller → Service → Repository)**

---

# 🛠 Tech Stack

### Backend

* **Java 17**
* **Spring Boot**
* **Spring Security**
* **JWT Authentication**
* **Spring Data JPA**
* **Hibernate**

### Database

* PostgreSQL / MySQL

### Tools

* Docker
* Git
* GitHub
* Maven
* IntelliJ IDEA
* Postman

---

# 🏗 System Architecture

The backend follows a **layered architecture** for clean separation of concerns.

```mermaid
flowchart TD

A[Client / Browser / Postman]

A --> B[Spring Boot REST Controller]

B --> C[Service Layer<br>Business Logic]

C --> D[Repository Layer<br>Spring Data JPA]

D --> E[(PostgreSQL / MySQL Database)]

B --> F[Spring Security]

F --> G[JWT Authentication Filter]

G --> B
```

# 🔐 JWT Authentication Flow

```mermaid
sequenceDiagram

participant Client
participant AuthController
participant AuthService
participant JWT
participant API

Client->>AuthController: Login Request (username, password)

AuthController->>AuthService: Validate credentials

AuthService->>JWT: Generate JWT Token

JWT-->>Client: Return JWT Token

Client->>API: Request with JWT Header

API->>JWT: Validate Token

JWT-->>API: Token Valid

API-->>Client: Return Protected Data
```


---

# 📂 Project Structure

```
college-erp-backend
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.college.erp
│   │   │        ├── controller
│   │   │        ├── service
│   │   │        ├── repository
│   │   │        ├── entity
│   │   │        ├── dto
│   │   │        └── config
│   │   │
│   │   └── resources
│   │        ├── application.properties
│   │        └── static
│
├── Dockerfile
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
# 🗄️ Database ER Diagram

The following diagram represents the **database structure of the College ERP Backend System**, including students, teachers, subjects, branches, and authentication users.

```mermaid
erDiagram

    BRANCH {
        varchar id PK
        varchar name
        varchar code
        varchar description
    }

    SUBJECT {
        varchar code PK
        varchar name
        int semester
        int credits
        varchar branchId FK
    }

    STUDENT {
        varchar id PK
        varchar firstName
        varchar lastName
        varchar email
        long phone
        long rollNo
        long enrollmentNumber
        int semester
        varchar city
        varchar state
        varchar branchId FK
        timestamp createdAt
        timestamp updatedAt
    }

    TEACHER {
        varchar id PK
        varchar firstName
        varchar lastName
        varchar email
        long phone
        varchar designation
        varchar branchId FK
        timestamp createdAt
        timestamp updatedAt
    }

    USERS {
        varchar id PK
        varchar username
        varchar password
        enum role
        varchar studentId
        varchar teacherId
        boolean enabled
        timestamp createdAt
        timestamp updatedAt
    }

    TEACHER_SUBJECT {
        varchar teacherId FK
        varchar subjectCode FK
    }

    BRANCH ||--o{ SUBJECT : offers
    BRANCH ||--o{ STUDENT : has
    BRANCH ||--o{ TEACHER : employs

    TEACHER ||--o{ TEACHER_SUBJECT : teaches
    SUBJECT ||--o{ TEACHER_SUBJECT : assigned

    STUDENT ||--o| USERS : account
    TEACHER ||--o| USERS : account
```


# 🐳 Docker Deployment Architecture

```mermaid
flowchart LR

A[Developer Machine]

A --> B[Docker Build]

B --> C[Docker Image]

C --> D[Docker Container]

D --> E[Spring Boot Application]

E --> F[(Database)]
```


---

# ⚙ Installation & Setup

### 1️⃣ Clone the repository

```
git clone https://github.com/yourusername/college-erp-backend.git
```

### 2️⃣ Navigate to project

```
cd college-erp-backend
```

### 3️⃣ Run the application

Using Maven wrapper:

```
./mvnw spring-boot:run
```

Or using Maven:

```
mvn spring-boot:run
```

Application will start on:

```
http://localhost:8080
```

---

# 🐳 Run with Docker

### Build Docker image

```
docker build -t college-erp-backend .
```

### Run Docker container

```
docker run -p 8080:8080 college-erp-backend
```

Now access the API at:

```
http://localhost:8080
```

---

# 🔑 Authentication

This project uses **JWT (JSON Web Token)** based authentication.

### Login Flow

1. User logs in with credentials
2. Server generates a **JWT token**
3. Client sends token in request headers

Example:

```
Authorization: Bearer <your-token>
```

---

# 📡 Example API Endpoints

| Method | Endpoint        | Description       |
| ------ | --------------- | ----------------- |
| POST   | /api/auth/login | Authenticate user |
| GET    | /api/students   | Get all students  |
| POST   | /api/students   | Create student    |
| GET    | /api/teachers   | Get all teachers  |
| POST   | /api/subjects   | Add subject       |

---

# 📑 API Documentation (Swagger)

This project uses **Swagger OpenAPI** for interactive API documentation.

After running the project open:

```
http://localhost:8080/swagger-ui.html
```

or

```
http://localhost:8080/swagger-ui/index.html
```

Swagger allows you to:

• Explore all available APIs
• Test endpoints directly from the browser
• View request and response schemas

---

# 🔐 Security

Security is implemented using **Spring Security and JWT authentication**.

Features:

* Stateless authentication
* Secure API endpoints
* Role based authorization
* Token based request validation

---

# 🧪 Testing APIs

You can test the APIs using:

* **Postman**
* **Swagger UI**
* **cURL**

Example:

```
curl -X GET http://localhost:8080/api/students
```

---

# 📌 Future Improvements

* Refresh token authentication
* Email verification
* Role management dashboard
* Microservice architecture
* Frontend integration (React / Angular)

---

# 🤝 Contributing

Contributions are welcome.

1. Fork the repository
2. Create a new branch
3. Commit your changes
4. Submit a Pull Request

---

# 👨‍💻 Author

**Ronit Kumar Verma**

B.Tech Computer Science
Backend Developer (Java | Spring Boot | Docker)

GitHub:
https://github.com/ronitverma28

---

# ⭐ Support

If you like this project, please give it a **⭐ on GitHub**.
