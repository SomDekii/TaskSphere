# Design Pattern Class Diagrams - Tabular Form

## Factory Pattern

| Class / Interface | Type | Main Members | Relationship |
|---|---|---|---|
| `TaskServiceImpl` | Service class | `taskFactory`, `createTask(...)` | Uses `TaskFactory` to create tasks and task behavior |
| `TaskFactory` | Factory class | `createTask(...)`, `create(...)` | Creates `Task` objects and returns `TaskBehavior` implementations |
| `TaskBehavior` | Interface | `creationMessage(title)` | Implemented by task-type behavior classes |
| `WorkTask` | Concrete class | `creationMessage(title)` | Implements `TaskBehavior` |
| `StudyTask` | Concrete class | `creationMessage(title)` | Implements `TaskBehavior` |
| `PersonalTask` | Concrete class | `creationMessage(title)` | Implements `TaskBehavior` |
| `Task` | Entity class | `id`, `title`, `status`, `taskType`, `userId` | Product object created by `TaskFactory` |

| Relationship | Meaning |
|---|---|
| `TaskServiceImpl -> TaskFactory` | Service delegates task creation to the factory |
| `TaskFactory -> Task` | Factory creates the task entity |
| `TaskFactory -> TaskBehavior` | Factory selects behavior based on task type |
| `WorkTask / StudyTask / PersonalTask -> TaskBehavior` | Concrete behaviors implement the common interface |

## Observer Pattern

| Class / Interface | Type | Main Members | Relationship |
|---|---|---|---|
| `TaskServiceImpl` | Service class | `taskEventPublisher` | Publishes task lifecycle events |
| `TaskEventPublisher` | Publisher class | `taskCreated(...)`, `taskStatusUpdated(...)`, `taskAssigned(...)`, `deadlineApproaching(...)` | Sends events to `NotificationSubject` |
| `Subject` | Interface | `register(...)`, `unregister(...)`, `notifyObservers(...)` | Defines subject behavior |
| `NotificationSubject` | Concrete subject | `observers`, `register(...)`, `unregister(...)`, `notifyObservers(...)` | Stores and notifies observers |
| `Observer` | Interface | `update(event)` | Defines observer behavior |
| `EmailNotificationObserver` | Concrete observer | `notificationAdapterRegistry`, `userRepository`, `update(event)` | Sends email notification reactions |
| `InAppNotificationObserver` | Concrete observer | `notificationRepository`, `notificationManager`, `notificationAdapterRegistry`, `update(event)` | Saves and publishes in-app notifications |
| `TaskEvent` | Event data class | `userId`, `taskId`, `message`, `type` | Carries event data from publisher to observers |

| Relationship | Meaning |
|---|---|
| `TaskServiceImpl -> TaskEventPublisher` | Service publishes task events |
| `TaskEventPublisher -> NotificationSubject` | Publisher delegates notification broadcasting |
| `NotificationSubject -> Observer` | Subject stores many observers |
| `EmailNotificationObserver -> Observer` | Email observer implements observer behavior |
| `InAppNotificationObserver -> Observer` | In-app observer implements observer behavior |
| `NotificationSubject -> TaskEvent` | Subject notifies observers using event data |

## Singleton Pattern

| Class / Interface | Type | Main Members | Relationship |
|---|---|---|---|
| `DatabaseConnectionManager` | Spring singleton component | `mongoClient`, `databaseName`, `database()` | Provides shared MongoDB database access |
| `MongoClient` | Dependency | Spring-managed Mongo client | Injected into `DatabaseConnectionManager` |
| `MongoDatabase` | Returned object | Mongo database instance | Returned by `database()` |

| Relationship | Meaning |
|---|---|
| `DatabaseConnectionManager -> MongoClient` | Uses one shared Mongo client managed by Spring |
| `DatabaseConnectionManager -> MongoDatabase` | Returns the configured database instance |

## Strategy Pattern

| Class / Interface | Type | Main Members | Relationship |
|---|---|---|---|
| `TaskServiceImpl` | Service class | `taskStrategyRegistry`, `getTasks(...)` | Applies filtering and sorting through registry |
| `TaskStrategyRegistry` | Context / registry class | `filterStrategies`, `sortStrategies`, `applyFilter(...)`, `applySort(...)` | Selects the correct strategy by key |
| `TaskFilterStrategy` | Interface | `supports(key)`, `filter(tasks, value)` | Common contract for filter strategies |
| `TaskSortStrategy` | Interface | `supports(key)`, `sort(tasks)` | Common contract for sort strategies |
| `TaskStrategy` | Interface | `apply(tasks)` | General task strategy contract |
| `StatusFilterStrategy` | Concrete strategy | `supports(...)`, `filter(...)` | Filters tasks by status |
| `PriorityFilterStrategy` | Concrete strategy | `supports(...)`, `filter(...)` | Filters tasks by priority |
| `TaskTypeFilterStrategy` | Concrete strategy | `supports(...)`, `filter(...)` | Filters tasks by type |
| `AssigneeFilterStrategy` | Concrete strategy | `supports(...)`, `filter(...)` | Filters tasks by assignee / user owner |
| `DeadlineSortStrategy` | Concrete strategy | `supports(...)`, `sort(...)`, `apply(...)` | Sorts tasks by deadline |
| `PrioritySortStrategy` | Concrete strategy | `supports(...)`, `sort(...)`, `apply(...)` | Sorts tasks by priority |
| `CompletedTaskStrategy` | Concrete strategy | `apply(...)` | Selects completed tasks |
| `PendingTaskStrategy` | Concrete strategy | `apply(...)` | Selects pending tasks |

| Relationship | Meaning |
|---|---|
| `TaskServiceImpl -> TaskStrategyRegistry` | Service asks registry to filter and sort |
| `TaskStrategyRegistry -> TaskFilterStrategy` | Registry stores and selects filter strategies |
| `TaskStrategyRegistry -> TaskSortStrategy` | Registry stores and selects sort strategies |
| `StatusFilterStrategy / PriorityFilterStrategy / TaskTypeFilterStrategy / AssigneeFilterStrategy -> TaskFilterStrategy` | Filter classes implement the filter strategy interface |
| `DeadlineSortStrategy / PrioritySortStrategy -> TaskSortStrategy` | Sort classes implement the sort strategy interface |
| `DeadlineSortStrategy / PrioritySortStrategy / CompletedTaskStrategy / PendingTaskStrategy -> TaskStrategy` | These classes implement the general task strategy interface |

## State Pattern

| Class / Interface | Type | Main Members | Relationship |
|---|---|---|---|
| `TaskServiceImpl` | Service class | `taskStateFactory`, `updateStatus(...)` | Uses current state to validate status transition |
| `TaskStateFactory` | Factory class | `from(status)` | Creates the correct `TaskState` object |
| `TaskState` | Interface | `canMoveTo(nextStatus)` | Defines transition validation behavior |
| `PendingState` | Concrete state | `canMoveTo(nextStatus)` | Allows `PENDING` or `IN_PROGRESS` |
| `InProgressState` | Concrete state | `canMoveTo(nextStatus)` | Allows `IN_PROGRESS` or `COMPLETED` |
| `CompletedState` | Concrete state | `canMoveTo(nextStatus)` | Allows `COMPLETED` or `ARCHIVED` |
| `ArchivedState` | Concrete state | `canMoveTo(nextStatus)` | Allows only `ARCHIVED` |
| `TaskStatus` | Enum | `PENDING`, `IN_PROGRESS`, `COMPLETED`, `ARCHIVED` | Status values checked by states |

| Relationship | Meaning |
|---|---|
| `TaskServiceImpl -> TaskStateFactory` | Service asks for current state behavior |
| `TaskStateFactory -> TaskState` | Factory returns the correct state implementation |
| `PendingState / InProgressState / CompletedState / ArchivedState -> TaskState` | Concrete states implement transition rules |
| `TaskState -> TaskStatus` | State compares the requested next status |

## Adapter Pattern

| Class / Interface | Type | Main Members | Relationship |
|---|---|---|---|
| `EmailNotificationObserver` | Client class | `notificationAdapterRegistry`, `update(event)` | Sends email through adapter registry |
| `InAppNotificationObserver` | Client class | `notificationAdapterRegistry`, `update(event)` | Sends in-app notification through adapter registry |
| `NotificationAdapterRegistry` | Registry / adapter selector | `adapters`, `send(channel, recipient, message)` | Selects adapter by notification channel |
| `NotificationAdapter` | Interface | `send(recipient, message)` | Common adapter contract |
| `EmailAdapter` | Concrete adapter | `send(recipient, message)` | Adapts email notification sending |
| `SMSAdapter` | Concrete adapter | `send(recipient, message)` | Adapts SMS notification sending |
| `PushAdapter` | Concrete adapter | `send(recipient, message)` | Adapts push notification sending |
| `InAppAdapter` | Concrete adapter | `send(recipient, message)` | Adapts in-app notification sending |
| `NotificationChannel` | Enum | `EMAIL`, `SMS`, `PUSH`, `IN_APP` | Channel key used to select adapter |

| Relationship | Meaning |
|---|---|
| `EmailNotificationObserver -> NotificationAdapterRegistry` | Observer sends email through registry |
| `InAppNotificationObserver -> NotificationAdapterRegistry` | Observer sends in-app notification through registry |
| `NotificationAdapterRegistry -> NotificationAdapter` | Registry stores concrete adapters behind common interface |
| `NotificationAdapterRegistry -> NotificationChannel` | Registry selects adapter by channel enum |
| `EmailAdapter / SMSAdapter / PushAdapter / InAppAdapter -> NotificationAdapter` | Concrete adapters implement common send method |
