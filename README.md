# DoneIt

[![CI Status](https://github.com/AlexisCnockaert/Done-It/workflows/Simple%20CI/badge.svg)](https://github.com/AlexisCnockaert/Done-It/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A glowing **To Do List** application built with **React**, **Spring Boot** and **NoSQL (MongoDB)**, featuring **AI-powered task generation**.
The goal: create a futuristic productivity assistant that guides the user through their tasks.

### Live Demo

[https://done-it.onrender.com](https://done-it.onrender.com)

<img width="1871" height="1009" alt="DoneIt screenshot" src="https://github.com/user-attachments/assets/8e778b32-714f-43fb-8f75-0280c9ef8651" />

##  Features

-  **Full CRUD operations** for task management
- 🤖 **AI-powered task generation** using the OpenRouter API
-  **JWT authentication** with secure user accounts
-  **Multi-page React app** with client-side routing (React Router)
-  **Docker support** for easy deployment
-  **CI pipeline** with automated testing

##  Architecture

```
React SPA (React Router)  ──REST/JSON──▶  Spring Boot API (/api)  ──▶  MongoDB
                                                  │
                                                  └──▶  OpenRouter API (LLM)
```

- The **frontend** is a React single-page application that consumes the REST API and handles navigation with React Router.
- The **backend** is a stateless Spring Boot (Java 21) REST API, secured with Spring Security and JWT.
- Tasks and user accounts are persisted in **MongoDB**.
- The **AI integration** is handled on the backend: it calls a large language model through the OpenRouter API to generate tasks. The API key is provided through the `IA_API_KEY` environment variable.

## 🛠️ Tech Stack

### Frontend
- ⚛️ React (modern hooks + functional components)
-  React Router for client-side routing
-  Custom CSS neon theme with glowing UI

### Backend
- ☕ Spring Boot (Java 21)
-  MongoDB for persistent task and user storage
-  Spring Security + JWT authentication
-  AI model integration (OpenRouter API)
-  RESTful API architecture

### DevOps
- 🐳 Docker & Docker Compose
-  GitHub Actions CI
- 🧪 Automated testing

##  Project Structure

```
Done-It/
├── backend/             # Spring Boot REST API
├── frontend/            # React application
├── .github/workflows/   # CI pipeline (GitHub Actions)
└── docker-compose.yml   # Full stack: frontend, backend, MongoDB
```

## 🚀 Local Development

### Prerequisites

- [Docker](https://docs.docker.com/get-docker/) installed
- [Docker Compose](https://docs.docker.com/compose/install/) installed

### 1. Create the environment file

Create a `.env` file at the root of the project:

```env
MONGO_USERNAME=admin
MONGO_PASSWORD=your_secure_password
JWT_SECRET=your_super_long_jwt_secret_min_32_characters
IA_API_KEY=your_openrouter_api_key
```

### 2. Start the application

```bash
docker-compose up --build -d
```

### 3. Access the application

- 🖥️ Frontend: [http://localhost](http://localhost)
- ⚙️ Backend API: [http://localhost:8080/api](http://localhost:8080/api)
- 🍃 MongoDB: `localhost:27017`

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
