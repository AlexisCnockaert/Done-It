#  ToDone


A glowing **To Do List** application built with **React**, **Spring Boot** and **NoSQL(MongoDB)** implementing **AI Model Features**, .\
The goal: create a futuristic productivity assistant that guide the user through its tasks.

## Tech Stack

**Frontend**
- ⚛️ React (modern hooks + functional components)
- Custom CSS neon theme with glowing UI

**Backend**
- ☕ Spring Boot (Java 21)
- MongoDB for persistent task storage
- RESTful API for todo management

## Setup Guide

Run the backend, frontend, and MongoDB locally.

### Prerequisites

- Java 21 & Maven  
- Node.js & npm  
- MongoDB Community Edition

### Start MongoDB

```bash
# Linux / macOS
mongod --dbpath ~/mongodb/data

# Windows
"C:\Program Files\MongoDB\Server\6.0\bin\mongod.exe" --dbpath C:\mongodb\data
```
### Run SpringBoot Backend
```bash
mvn clean install
mvn spring-boot:run
```

### Run React Frontend ( on new terminal )
```bash
cd frontend
npm install
npm start
```

