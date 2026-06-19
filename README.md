# 🚀 Fleetsync

**A modern, microservices-based fleet management system built with Spring Boot and React.**

Fleetsync is a comprehensive solution for managing vehicle fleets with features including user authentication, job scheduling, and real-time data synchronization across multiple services.

---

## 📋 Table of Contents

- [Features](#features)
- [Project Architecture](#project-architecture)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Contributing](#contributing)


---

## ✨ Features

### Authentication Service
- ✅ User registration and login
- ✅ JWT-based authentication
- ✅ Token refresh mechanism with Redis
- ✅ Role-based access control (ADMIN, USER)
- ✅ Secure password hashing with BCrypt
- ✅ Token validation for inter-service communication

### Job Service
- 📅 Job scheduling and management
- 🔔 Real-time job status updates
- 📊 Job analytics and reporting

### Coming Soon
- 📍 Real-time GPS tracking
- 🗺️ Route optimization
- 💬 Driver communication
- 📱 Mobile app support

---

## 🏗️ Project Architecture

Fleetsync follows a **microservices architecture** with independent, scalable services:

```
┌─────────────────────────────────────────────────────────┐
│                     Frontend (React)                      │
└────────────────┬────────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────────┐
│              API Gateway (Coming Soon)                    │
└─┬──────────────────────────────┬──────────────────────┬─┘
  │                              │                      │
  ▼                              ▼                      ▼
┌──────────────────┐   ┌─────────────────┐   ┌──────────────┐
│  Auth Service    │   │  Job Service    │   │ More Services│
│  (Port: 8080)    │   │  (Port: 8081)   │   │              │
│                  │   │                 │   │              │
│ • Register       │   │ • Create Jobs   │   │ (Planned)    │
│ • Login          │   │ • Track Status  │   │              │
│ • Validate Token │   │ • Analytics     │   │              │
└────────┬─────────┘   └────────┬────────┘   └──────────────┘
         │                      │
    ┌────▼──────────────────────▼────┐
    │  Databases                      │
    │  ├─ MySQL (User Data, Jobs)    │
    │  └─ Redis (Sessions, Tokens)   │
    └─────────────────────────────────┘
```

---

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Security**: Spring Security, JWT (JJWT)
- **Database**: MySQL 8.0+
- **Cache**: Redis 6.0+
- **Build Tool**: Maven
- **IDE**: IntelliJ IDEA

### Frontend
- **Framework**: React 18.x
- **Styling**: CSS/Tailwind CSS (Planned)
- **State Management**: Redux (Planned)
- **HTTP Client**: Axios

### Infrastructure
- **Containerization**: Docker
- **Orchestration**: Docker Compose
- **CI/CD**: GitHub Actions (Planned)

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

| Software | Version | Purpose |
|----------|---------|---------|
| Java | 17+ | Backend runtime |
| Maven | 3.8+ | Dependency management |
| MySQL | 8.0+ | Primary database |
| Redis | 6.0+ | Session/token storage |
| Node.js | 16+ | Frontend development |
| npm or yarn | Latest | Package management |
| Docker | 20.10+ | Containerization |
| Git | 2.30+ | Version control |

### Verify Installation
```bash
java -version
mvn -version
mysql --version
redis-cli --version
node -version
npm -version
docker --version
```

---

## 💻 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/shreyash609/Fleetsync.git
cd Fleetsync
```

### 2. Database Setup

#### MySQL Configuration
```bash
# Start MySQL server (if not already running)
mysql -u root -p

# Create database
CREATE DATABASE fleetsync;
CREATE USER 'fleetsync_user'@'localhost' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON fleetsync.* TO 'fleetsync_user'@'localhost';
FLUSH PRIVILEGES;
```

#### Update Application Properties
Edit `services/auth-service/src/main/resources/application.properties`:
```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/fleetsync?useSSL=false&serverTimezone=UTC
spring.datasource.username=fleetsync_user
spring.datasource.password=your_secure_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT Configuration
jwt.secret=your_super_secret_key_at_least_32_characters_long_for_security
jwt.expiration=3600000

# Redis Configuration
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=
```

### 3. Redis Setup
```bash
# Start Redis server
redis-server

# Verify Redis is running
redis-cli ping
# Expected output: PONG
```

### 4. Backend Setup
```bash
# Navigate to auth-service
cd services/auth-service

# Install dependencies
mvn clean install

# Build the project
mvn clean package
```

### 5. Frontend Setup
```bash
# Navigate to frontend
cd frontend

# Install dependencies
npm install

# Install additional packages if needed
npm install axios react-router-dom
```

---

## 🚀 Running the Application

### Option 1: Run Services Individually

#### Start MySQL
```bash
mysql -u fleetsync_user -p
```

#### Start Redis
```bash
redis-server
```

#### Start Auth Service
```bash
cd services/auth-service
mvn spring-boot:run
# Service will run on http://localhost:8080
```

#### Start Job Service
```bash
cd services/job-service
mvn spring-boot:run
# Service will run on http://localhost:8081
```

#### Start Frontend
```bash
cd frontend
npm start
# App will open on http://localhost:3000
```

### Option 2: Run with Docker Compose (Recommended)
```bash
# Navigate to infrastructure directory
cd infrastructure

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### Verify Services are Running
```bash
# Check Auth Service
curl http://localhost:8080/actuator/health

# Check Job Service
curl http://localhost:8081/actuator/health

# Check Frontend
open http://localhost:3000
```

---

## 📚 API Documentation

### Authentication Service (Port: 8080)

#### Register User
```http
POST /auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePassword123!",
  "role": "USER"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "tokenType": "bearer",
  "name": "John Doe",
  "role": "USER"
}
```

#### Login User
```http
POST /auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "SecurePassword123!"
}
```

#### Refresh Token
```http
POST /auth/refresh
X-Refresh-Token: f47ac10b-58cc-4372-a567-0e02b2c3d479
```

#### Validate Token
```http
GET /auth/validate
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Logout
```http
POST /auth/logout
X-Refresh-Token: f47ac10b-58cc-4372-a567-0e02b2c3d479
```

### Error Responses
All endpoints return standardized error responses:

```json
{
  "errorCode": "INVALID_CREDENTIALS",
  "message": "Invalid email or password",
  "timestamp": "2026-06-19T12:30:45",
  "path": "/auth/login"
}
```

---

## 📁 Project Structure

```
Fleetsync/
├── services/
│   ├── auth-service/
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/com/fleetsync/auth_service/
│   │   │   │   │   ├── controller/
│   │   │   │   │   │   └── AuthController.java
│   │   │   │   │   ├── service/
│   │   │   │   │   │   └── AuthService.java
│   │   │   │   │   ├── entity/
│   │   │   │   │   │   └── User.java
│   │   │   │   │   ├── repository/
│   │   │   │   │   │   └── UserRepository.java
│   │   │   │   │   ├── security/
│   │   │   │   │   │   ├── JwtUtils.java
│   │   │   │   │   │   └── SecurityConfig.java
│   │   │   │   │   ├── exception/
│   │   │   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   │   │   └── Custom Exceptions...
│   │   │   │   │   └── dto/
│   │   │   │   │       ├── RegisterRequest.java
│   │   │   │   │       ├── LoginRequest.java
│   │   │   │   │       └── AuthResponse.java
│   │   │   │   └── resources/
│   │   │   │       └── application.properties
│   │   │   └── test/
│   │   │       └── java/...
│   │   └── pom.xml
│   │
│   └── job-service/
│       ├── src/...
│       └── pom.xml
│
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── App.js
│   │   └── index.js
│   ├── package.json
│   └── .env
│
├── infrastructure/
│   ├── docker-compose.yml
│   ├── Dockerfile (auth-service)
│   └── Dockerfile (job-service)
│
├── README.md
└── .gitignore
```

---

## ⚙️ Configuration

### Environment Variables

Create a `.env` file in the project root:

```bash
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=fleetsync
DB_USER=fleetsync_user
DB_PASSWORD=your_password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT
JWT_SECRET=your_super_secret_key_minimum_32_characters
JWT_EXPIRATION=3600000

# API
API_PORT=8080
FRONTEND_URL=http://localhost:3000
```

### Application Properties

**Auth Service** (`services/auth-service/src/main/resources/application.properties`):
```properties
spring.application.name=auth-service
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/fleetsync
spring.datasource.username=fleetsync_user
spring.datasource.password=your_password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}

# Redis
spring.redis.host=localhost
spring.redis.port=6379

# Actuator
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always
```

---

## 🔒 Security Best Practices

1. **Never commit sensitive data** (passwords, API keys) to version control
2. **Use environment variables** for configuration
3. **Enable HTTPS** in production
4. **Use strong JWT secrets** (minimum 32 characters)
5. **Implement rate limiting** on auth endpoints
6. **Enable CORS** only for trusted domains
7. **Keep dependencies updated** - run `mvn versions:display-dependency-updates`
8. **Use prepared statements** to prevent SQL injection

---

## 🧪 Testing

### Run Unit Tests
```bash
cd services/auth-service
mvn test
```

### Run Integration Tests
```bash
mvn verify
```

### Check Code Coverage
```bash
mvn jacoco:report
# Report generated at: target/site/jacoco/index.html
```

---

## 📝 Git Workflow

### Create a Feature Branch
```bash
git checkout -b feature/your-feature-name
git add .
git commit -m "feat: Add your feature description"
git push origin feature/your-feature-name
```

### Create a Bugfix Branch
```bash
git checkout -b fix/your-bug-fix
git add .
git commit -m "fix: Fix your bug description"
git push origin fix/your-bug-fix
```

### Commit Message Convention
```
feat: Add new feature
fix: Fix a bug
docs: Update documentation
style: Code style changes
refactor: Code refactoring
test: Add/update tests
chore: Dependency updates
```

---

## 🚧 Roadmap

- [x] Auth Service (JWT, Registration, Login)
- [x] Job Service (Basic structure)
- [ ] API Gateway (Service routing)
- [ ] Real-time GPS tracking
- [ ] Route optimization
- [ ] Mobile application
- [ ] Admin dashboard
- [ ] Notification service
- [ ] Analytics service
- [ ] Kubernetes deployment

---

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Standards
- Follow Java naming conventions
- Write meaningful commit messages
- Add unit tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR


---

## 👥 Team & Contact

**Project Lead**: Shreyash Sarve  
**Email**: shreyash.sarve04@gmail.com  
**GitHub**: [@shreyash609](https://github.com/shreyash609)

---

## 🆘 Troubleshooting

### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### MySQL Connection Error
```bash
# Verify MySQL is running
mysql -u root -p

# Check connection string in application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/fleetsync
```

### Redis Connection Error
```bash
# Check Redis is running
redis-cli ping

# Should return: PONG
```

### JWT Secret Too Short
```bash
# Generate a secure secret
openssl rand -hex 32
# Use this value in application.properties
```

---

## 📞 Support

For issues and questions:
- 📧 Email: shreyash.sarve04@gmail.com
- 🐛 GitHub Issues: [Create an issue](https://github.com/shreyash609/Fleetsync/issues)
- 💬 Discussions: [Start a discussion](https://github.com/shreyash609/Fleetsync/discussions)

---

**Last Updated**: June 2026  
**Version**: 0.1.0  
**Status**: 🚀 In Active Development
