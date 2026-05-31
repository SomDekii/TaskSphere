# TaskSphere

TaskSphere is a task management app built with a Spring Boot backend, MongoDB, JWT authentication, real-time notifications, and a React/Vite frontend.

## Features

- User signup and login with JWT authentication
- Create, view, edit, delete, search, filter, and sort tasks
- Task status flow: `PENDING`, `IN_PROGRESS`, `COMPLETED`
- Dashboard with task statistics, completion progress, deadlines, and recent notifications
- In-app notifications with Server-Sent Events
- Email and push notification adapters with configurable delivery
- Backend design patterns: Factory, Observer, Adapter, Strategy, State, and Singleton

## Tech Stack
Backend:  Spring Boot, Spring Security, MongoDB
Frontend: React, Vite, Tailwind CSS
Auth:     JWT

## Project Structure
TaskSphere/
  Tasksphere/              # Spring Boot backend
  tasksphere-frontend/     # React frontend

## Prerequisites

- Java 17 or newer
- Node.js 18 or newer
- npm
- MongoDB local or MongoDB Atlas

## Backend Setup

The backend reads configuration from:
Tasksphere/.env
Example:
properties
MONGODB_URI=mongodb://localhost:27017/tasksphere
MONGODB_DATABASE=tasksphere

Optional email SMTP settings:

properties
EMAIL_NOTIFICATIONS_ENABLED=true
EMAIL_FROM=your-email@gmail.com
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USERNAME=your-email@gmail.com
SMTP_PASSWORD=your-app-password
SMTP_AUTH=true
SMTP_STARTTLS_ENABLE=true

## Run the App

Start the backend:
bash
cd D:/TaskSphere/Tasksphere
./mvnw.cmd spring-boot:run

Start the frontend:

bash
cd D:/TaskSphere/tasksphere-frontend
npm install
npm run dev

Open:

```txt
http://localhost:5173
```

Backend runs on:

```txt
http://localhost:8090
```

## Design Patterns Used

- **Factory Pattern:** creates tasks and task-type behavior.
- **Observer Pattern:** publishes task events to notification observers.
- **Adapter Pattern:** provides common notification sending for email, SMS, push, and in-app channels.
- **Strategy Pattern:** handles task filtering and sorting rules.
- **State Pattern:** validates task status transitions.
- **Singleton Pattern:** uses Spring-managed singleton components for shared services such as database access.
