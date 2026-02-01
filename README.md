# DoneIt

![CI Status](https://github.com/AlexisCnockaert/react-springboot-todolist/workflows/Simple%20CI/badge.svg)

A glowing **To Do List** application built with **React**, **Spring Boot** and **NoSQL (MongoDB)** implementing **AI Model Features**.
The goal: create a futuristic productivity assistant that guides the user through their tasks.

## Features

-  **Full CRUD operations** for task management
-  **AI-powered task generation** using OpenRouter API
-  **JWT Authentication** with secure user accounts
-  **Docker support** for easy deployment
-  **CI pipeline** with automated testing

##  Tech Stack

### Frontend
-  React  (modern hooks + functional components)
-  Custom CSS neon theme with glowing UI

### Backend
- ☕ Spring Boot (Java 21)
-  MongoDB for persistent task storage
-  Spring Security + JWT Authentication
-  AI Model integration (OpenRouter API)
-  RESTful API architecture

### DevOps
- 🐳 Docker
-  GitHub Actions CI
-  Automated testing

### Prerequisites

- [Docker](https://docs.docker.com/get-docker/) installed
- [Docker Compose](https://docs.docker.com/compose/install/) installed

### Local Development

1. **Create environment file**
   
   Create a `.env` file at the root of the project:
   ```env
   MONGO_USERNAME=admin
   MONGO_PASSWORD=your_secure_password
   JWT_SECRET=your_super_long_jwt_secret_min_32_characters
   IA_API_KEY=your_openrouter_api_key
   ```

2. **Start the application**
   ```bash
   docker-compose up --build -d
   ```

3. **Access the application**
   -  Frontend: [http://localhost](http://localhost)
   -  Backend API: [http://localhost:8080/api](http://localhost:8080/api)
   -  MongoDB: `localhost:27017`

### Useful Commands

```bash
# View logs
docker-compose logs -f

# Stop the application
docker-compose down

# Rebuild after changes
docker-compose up --build

# Remove all data (volumes)
docker-compose down -v
```
## 📝 License

This project is licensed under the MIT License.
---

⭐ If you found this project helpful, please consider giving it a star!
