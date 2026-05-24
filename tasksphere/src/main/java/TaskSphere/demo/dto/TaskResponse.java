package TaskSphere.demo.dto;

import TaskSphere.demo.entity.Task;
import TaskSphere.demo.entity.TaskPriority;
import TaskSphere.demo.entity.TaskStatus;
import TaskSphere.demo.entity.TaskType;

import java.time.LocalDateTime;

public class TaskResponse {
    private String id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private TaskType taskType;

    public static TaskResponse from(Task task) {
        TaskResponse response = new TaskResponse();
        response.id = task.getId();
        response.title = task.getTitle();
        response.description = task.getDescription();
        response.deadline = task.getDeadline();
        response.priority = task.getPriority();
        response.status = task.getStatus();
        response.createdAt = task.getCreatedAt();
        response.updatedAt = task.getUpdatedAt();
        response.taskType = task.getTaskType();
        return response;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getDeadline() { return deadline; }
    public TaskPriority getPriority() { return priority; }
    public TaskStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public TaskType getTaskType() { return taskType; }
}
