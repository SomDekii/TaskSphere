# TaskSphere

TaskSphere is a full-stack task management application with a Spring Boot backend, MongoDB persistence, JWT authentication, real-time in-app notifications, and a React/Vite frontend.

## Features

- User registration and login with JWT authentication
- Protected dashboard and task pages
- Create, edit, view, delete, search, filter, and sort tasks
- Task status transitions: `PENDING`, `IN_PROGRESS`, and `COMPLETED`
- Upcoming deadlines and recent notifications on the dashboard
- In-app notifications with server-sent events support
- Design pattern integrations in the backend:
  - Factory Pattern for task creation
  - Observer Pattern for task events and notifications
  - Singleton Pattern wrapper for MongoDB access
  - Strategy Pattern for task filtering and sorting
  - State Pattern for task status transitions
  - Adapter Pattern for notification channels

## Project Structure

```txt
TaskSphere/
  Tasksphere/              # Spring Boot backend
    src/main/java/
    src/main/resources/
    src/test/java/
    pom.xml
    mvnw.cmd

  tasksphere-frontend/     # React + Vite frontend
    src/
    index.html
    package.json
    vite.config.js
    tailwind.config.js
```

> Note: On Windows, `Tasksphere` and `tasksphere` may resolve to the same folder because paths are case-insensitive.

## Prerequisites

Install these before running the project:

- Java 17 or newer
- Node.js 18 or newer
- npm
- MongoDB, either local or MongoDB Atlas

## Backend Setup

The backend is located in:

```txt
Tasksphere/
```

### Backend Configuration

The main configuration file is:

```txt
Tasksphere/src/main/resources/application.properties
```

Current defaults:

```properties
spring.data.mongodb.uri=${MONGODB_URI:mongodb://localhost:27017/tasksphere}
spring.data.mongodb.database=${MONGODB_DATABASE:tasksphere}
server.port=8090
```

You can run with a local MongoDB database using the default settings.

For MongoDB Atlas or a custom database, create:

```txt
Tasksphere/.env
```

Example:

```properties
MONGODB_URI=mongodb+srv://username:password@cluster.example.mongodb.net/tasksphere
MONGODB_DATABASE=tasksphere
```

## Running the Full Application

Open two terminals.

Terminal 1:

```bash
cd D:/TaskSphere/Tasksphere
./mvnw.cmd spring-boot:run
```

Terminal 2:

```bash
cd D:/TaskSphere/tasksphere-frontend
npm run dev
```

Then open:

```txt
http://localhost:5173
```

## Main API Endpoints

Authentication:

```txt
POST /api/auth/signup
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/health
```

Tasks:

```txt
GET    /api/tasks
GET    /api/tasks/{id}
POST   /api/tasks
PUT    /api/tasks/{id}
PATCH  /api/tasks/{id}/status
DELETE /api/tasks/{id}
```

Dashboard:

```txt
GET /api/dashboard
```

Notifications:

```txt
GET    /api/notifications
GET    /api/notifications/stream
PATCH  /api/notifications/{id}/read
DELETE /api/notifications/{id}
```

## Task Query Parameters

`GET /api/tasks` supports:

```txt
search
status
priority
type
sortBy
assignee
```

Examples:

```txt
/api/tasks?status=PENDING
/api/tasks?priority=HIGH
/api/tasks?type=WORK
/api/tasks?sortBy=deadline
/api/tasks?sortBy=priority
```

## Design Patterns Used

The backend uses several design patterns to keep task creation, filtering, status handling, and notifications modular without changing the existing API behavior.

### Factory Pattern

Location:

```txt
Tasksphere/src/main/java/TaskSphere/demo/service/factory/
```

Purpose:

- Creates `Task` objects in one central place.
- Creates task-type behavior for `PERSONAL`, `WORK`, and `STUDY` tasks.
- Makes it easier to add new task types later without rewriting task creation logic.

Used in:

```txt
TaskServiceImpl.createTask(...)
```

Example use:

```java
Task task = taskFactory.createTask(request, userId, now);
```

### Observer Pattern

Location:

```txt
Tasksphere/src/main/java/TaskSphere/demo/service/observer/
```

Purpose:

- Publishes task events such as task creation, task updates, status changes, assignment events, and deadline reminders.
- Lets multiple notification handlers react to task events independently.
- Keeps task business logic separate from notification delivery logic.

Main classes:

```txt
TaskEvent.java
TaskEventPublisher.java
NotificationSubject.java
EmailNotificationObserver.java
InAppNotificationObserver.java
```

Used for:

- Task created notifications
- Task updated notifications
- Task status changed notifications
- Deadline reminder notifications
- Future task assignment notifications

### Singleton Pattern

Location:

```txt
Tasksphere/src/main/java/TaskSphere/demo/config/DatabaseConnectionManager.java
```

Purpose:

- Uses Spring's singleton bean lifecycle to keep one shared MongoDB client access point.
- Avoids manually creating duplicate database connection objects.
- Preserves the existing Spring Data MongoDB repository behavior.

Used for:

- Centralized MongoDB database access through the already configured Spring `MongoClient`.

### Strategy Pattern

Location:

```txt
Tasksphere/src/main/java/TaskSphere/demo/service/strategy/
```

Purpose:

- Separates filtering and sorting rules from `TaskServiceImpl`.
- Makes filters and sorting rules interchangeable.
- Allows new filters or sorts to be added without rewriting the task listing flow.

Strategies included:

```txt
StatusFilterStrategy.java      # filter by task status
PriorityFilterStrategy.java    # filter by priority
TaskTypeFilterStrategy.java    # filter by task type
AssigneeFilterStrategy.java    # filter by assignee/user owner
DeadlineSortStrategy.java      # sort by deadline
PrioritySortStrategy.java      # sort by priority
```

Used in:

```txt
TaskServiceImpl.getTasks(...)
```

Example use:

```java
tasks = taskStrategyRegistry.applyFilter("status", tasks, status);
tasks = taskStrategyRegistry.applySort(sortBy, tasks);
```

### State Pattern

Location:

```txt
Tasksphere/src/main/java/TaskSphere/demo/service/state/
```

Purpose:

- Encapsulates task status transition rules.
- Prevents invalid status changes cleanly.
- Keeps status transition behavior out of the main service logic.

States included:

```txt
PendingState.java
InProgressState.java
CompletedState.java
ArchivedState.java
TaskStateFactory.java
```

Supported statuses:

```txt
PENDING
IN_PROGRESS
COMPLETED
ARCHIVED
```

Used in:

```txt
TaskServiceImpl.updateStatus(...)
```

Example use:

```java
if (!taskStateFactory.from(task.getStatus()).canMoveTo(request.getStatus())) {
    throw new BadRequestException("Invalid task status transition");
}
```

### Adapter Pattern

Location:

```txt
Tasksphere/src/main/java/TaskSphere/demo/service/adapter/
```

Purpose:

- Provides a common interface for different notification delivery services.
- Allows email, SMS, push, and in-app notifications to be called through the same business-facing API.
- Makes it possible to replace mock providers with real providers later without changing task or notification business logic.

Adapters included:

```txt
EmailAdapter.java
SMSAdapter.java
PushAdapter.java
InAppAdapter.java
NotificationAdapterRegistry.java
```

Used by:

```txt
EmailNotificationObserver.java
InAppNotificationObserver.java
```

Example use:

```java
notificationAdapterRegistry.send(NotificationChannel.EMAIL, user.getEmail(), event.getMessage());
```

## Common Troubleshooting

### Vite Cannot Find Modules

If Vite reports missing chunks or missing packages, reinstall dependencies:

```bash
cd tasksphere-frontend
npm install
```

If the Vite cache is stale, delete the generated cache:

```bash
Remove-Item -Recurse -Force node_modules/.vite
```

Then restart:

````bash
npm run dev

### Backend Cannot Connect to MongoDB

Check that MongoDB is running locally:

```txt
mongodb://localhost:27017/tasksphere
````

Or set `MONGODB_URI` in `Tasksphere/.env`.

### Frontend Cannot Reach Backend

Make sure the backend is running on:

```txt
http://localhost:8090
```

Also check the frontend API base URL in:

```txt
tasksphere-frontend/src/services/api.js
```

### Java Validation Import Error

If VS Code shows:

```txt
The import jakarta.validation cannot be resolved
```

run:

```bash
cd Tasksphere
./mvnw.cmd clean compile
```

Then in VS Code run:

```txt
Java: Clean Java Language Server Workspace
```
