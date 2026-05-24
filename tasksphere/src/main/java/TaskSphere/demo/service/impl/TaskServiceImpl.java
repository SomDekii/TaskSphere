package TaskSphere.demo.service.impl;

import TaskSphere.demo.dto.StatusUpdateRequest;
import TaskSphere.demo.dto.TaskRequest;
import TaskSphere.demo.dto.TaskResponse;
import TaskSphere.demo.entity.NotificationType;
import TaskSphere.demo.entity.Task;
import TaskSphere.demo.entity.TaskStatus;
import TaskSphere.demo.exception.BadRequestException;
import TaskSphere.demo.exception.ResourceNotFoundException;
import TaskSphere.demo.repository.TaskRepository;
import TaskSphere.demo.service.TaskService;
import TaskSphere.demo.service.factory.TaskFactory;
import TaskSphere.demo.service.observer.TaskEventPublisher;
import TaskSphere.demo.service.state.TaskStateFactory;
import TaskSphere.demo.service.strategy.TaskStrategyRegistry;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskFactory taskFactory;
    private final TaskEventPublisher taskEventPublisher;
    private final TaskStateFactory taskStateFactory;
    private final TaskStrategyRegistry taskStrategyRegistry;

    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskFactory taskFactory,
                           TaskEventPublisher taskEventPublisher,
                           TaskStateFactory taskStateFactory,
                           TaskStrategyRegistry taskStrategyRegistry) {
        this.taskRepository = taskRepository;
        this.taskFactory = taskFactory;
        this.taskEventPublisher = taskEventPublisher;
        this.taskStateFactory = taskStateFactory;
        this.taskStrategyRegistry = taskStrategyRegistry;
    }

    @Override
    public List<TaskResponse> getTasks(String userId, String search, String status, String priority, String type, String sortBy, String assignee) {
        List<Task> tasks = search == null || search.isBlank()
                ? taskRepository.findByUserId(userId)
                : taskRepository.findByUserIdAndTitleContainingIgnoreCase(userId, search);

        tasks = taskStrategyRegistry.applyFilter("status", tasks, status);
        tasks = taskStrategyRegistry.applyFilter("priority", tasks, priority);
        tasks = taskStrategyRegistry.applyFilter("type", tasks, type);
        tasks = taskStrategyRegistry.applyFilter("assignee", tasks, assignee);
        tasks = taskStrategyRegistry.applySort(sortBy, tasks);
        return tasks.stream().map(TaskResponse::from).toList();
    }

    @Override
    public TaskResponse getTask(String userId, String id) {
        return TaskResponse.from(findUserTask(userId, id));
    }

    @Override
    public TaskResponse createTask(String userId, TaskRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Task task = taskFactory.createTask(request, userId, now);
        Task saved = taskRepository.save(task);
        String message = taskFactory.create(saved.getTaskType()).creationMessage(saved.getTitle());
        taskEventPublisher.taskCreated(userId, saved.getId(), message);
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
        taskEventPublisher.taskStatusUpdated(userId, saved.getId(), "Task updated: " + saved.getTitle(), NotificationType.TASK_UPDATED);
        return TaskResponse.from(saved);
    }

    @Override
    public TaskResponse updateStatus(String userId, String id, StatusUpdateRequest request) {
        Task task = findUserTask(userId, id);
        if (!taskStateFactory.from(task.getStatus()).canMoveTo(request.getStatus())) {
            throw new BadRequestException("Invalid task status transition");
        }
        task.setStatus(request.getStatus());
        task.setUpdatedAt(LocalDateTime.now());
        Task saved = taskRepository.save(task);
        NotificationType type = saved.getStatus() == TaskStatus.COMPLETED
                ? NotificationType.TASK_COMPLETED
                : NotificationType.TASK_UPDATED;
        taskEventPublisher.taskStatusUpdated(userId, saved.getId(), "Task status changed: " + saved.getTitle(), type);
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

}
