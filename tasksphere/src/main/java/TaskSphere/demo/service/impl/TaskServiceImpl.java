package TaskSphere.demo.service.impl;

import TaskSphere.demo.dto.StatusUpdateRequest;
import TaskSphere.demo.dto.TaskRequest;
import TaskSphere.demo.dto.TaskResponse;
import TaskSphere.demo.entity.NotificationType;
import TaskSphere.demo.entity.Task;
import TaskSphere.demo.entity.TaskPriority;
import TaskSphere.demo.entity.TaskStatus;
import TaskSphere.demo.entity.TaskType;
import TaskSphere.demo.exception.BadRequestException;
import TaskSphere.demo.exception.ResourceNotFoundException;
import TaskSphere.demo.repository.TaskRepository;
import TaskSphere.demo.service.TaskService;
import TaskSphere.demo.service.factory.TaskFactory;
import TaskSphere.demo.service.observer.NotificationSubject;
import TaskSphere.demo.service.state.CompletedState;
import TaskSphere.demo.service.state.InProgressState;
import TaskSphere.demo.service.state.PendingState;
import TaskSphere.demo.service.state.TaskState;
import TaskSphere.demo.service.strategy.CompletedTaskStrategy;
import TaskSphere.demo.service.strategy.DeadlineSortStrategy;
import TaskSphere.demo.service.strategy.PendingTaskStrategy;
import TaskSphere.demo.service.strategy.PrioritySortStrategy;
import TaskSphere.demo.service.strategy.TaskStrategy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskFactory taskFactory;
    private final NotificationSubject notificationSubject;

    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskFactory taskFactory,
                           NotificationSubject notificationSubject) {
        this.taskRepository = taskRepository;
        this.taskFactory = taskFactory;
        this.notificationSubject = notificationSubject;
    }

    @Override
    public List<TaskResponse> getTasks(String userId, String search, String status, String priority, String type, String sortBy) {
        List<Task> tasks = search == null || search.isBlank()
                ? taskRepository.findByUserId(userId)
                : taskRepository.findByUserIdAndTitleContainingIgnoreCase(userId, search);

        TaskStrategy filterStrategy = filterStrategy(status);
        if (filterStrategy != null) {
            tasks = filterStrategy.apply(tasks);
        } else if (hasText(status)) {
            TaskStatus selectedStatus = parseStatus(status);
            tasks = tasks.stream().filter(task -> task.getStatus() == selectedStatus).toList();
        }
        if (hasText(priority)) {
            TaskPriority selectedPriority = parsePriority(priority);
            tasks = tasks.stream().filter(task -> task.getPriority() == selectedPriority).toList();
        }
        if (hasText(type)) {
            TaskType selectedType = parseType(type);
            tasks = tasks.stream().filter(task -> task.getTaskType() == selectedType).toList();
        }
        TaskStrategy sortStrategy = sortStrategy(sortBy);
        if (sortStrategy != null) {
            tasks = sortStrategy.apply(tasks);
        }
        return tasks.stream().map(TaskResponse::from).toList();
    }

    @Override
    public TaskResponse getTask(String userId, String id) {
        return TaskResponse.from(findUserTask(userId, id));
    }

    @Override
    public TaskResponse createTask(String userId, TaskRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDeadline(request.getDeadline());
        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus() == null ? TaskStatus.PENDING : request.getStatus());
        task.setTaskType(request.getTaskType());
        task.setUserId(userId);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        Task saved = taskRepository.save(task);
        String message = taskFactory.create(saved.getTaskType()).creationMessage(saved.getTitle());
        notificationSubject.notifyObservers(userId, saved.getId(), message, NotificationType.TASK_CREATED);
        return TaskResponse.from(saved);
    }

    @Override
    public TaskResponse updateTask(String userId, String id, TaskRequest request) {
        Task task = findUserTask(userId, id);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDeadline(request.getDeadline());
        task.setPriority(request.getPriority());
        task.setTaskType(request.getTaskType());
        task.setUpdatedAt(LocalDateTime.now());
        Task saved = taskRepository.save(task);
        notificationSubject.notifyObservers(userId, saved.getId(), "Task updated: " + saved.getTitle(), NotificationType.TASK_UPDATED);
        return TaskResponse.from(saved);
    }

    @Override
    public TaskResponse updateStatus(String userId, String id, StatusUpdateRequest request) {
        Task task = findUserTask(userId, id);
        if (!stateFor(task.getStatus()).canMoveTo(request.getStatus())) {
            throw new BadRequestException("Invalid task status transition");
        }
        task.setStatus(request.getStatus());
        task.setUpdatedAt(LocalDateTime.now());
        Task saved = taskRepository.save(task);
        NotificationType type = saved.getStatus() == TaskStatus.COMPLETED
                ? NotificationType.TASK_COMPLETED
                : NotificationType.TASK_UPDATED;
        notificationSubject.notifyObservers(userId, saved.getId(), "Task status changed: " + saved.getTitle(), type);
        return TaskResponse.from(saved);
    }

    @Override
    public void deleteTask(String userId, String id) {
        Task task = findUserTask(userId, id);
        taskRepository.delete(task);
    }

    private Task findUserTask(String userId, String id) {
        return taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    private TaskStrategy filterStrategy(String filter) {
        if ("completed".equalsIgnoreCase(filter)) {
            return new CompletedTaskStrategy();
        }
        if ("pending".equalsIgnoreCase(filter)) {
            return new PendingTaskStrategy();
        }
        return null;
    }

    private TaskStrategy sortStrategy(String sort) {
        if ("priority".equalsIgnoreCase(sort)) {
            return new PrioritySortStrategy();
        }
        if ("deadline".equalsIgnoreCase(sort)) {
            return new DeadlineSortStrategy();
        }
        return null;
    }

    private TaskState stateFor(TaskStatus status) {
        return switch (status) {
            case PENDING -> new PendingState();
            case IN_PROGRESS -> new InProgressState();
            case COMPLETED -> new CompletedState();
        };
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private TaskStatus parseStatus(String value) {
        try {
            return TaskStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid status filter");
        }
    }

    private TaskPriority parsePriority(String value) {
        try {
            return TaskPriority.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid priority filter");
        }
    }

    private TaskType parseType(String value) {
        try {
            return TaskType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid type filter");
        }
    }
}
