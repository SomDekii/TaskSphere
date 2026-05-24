package TaskSphere.demo.dto;

import TaskSphere.demo.entity.TaskPriority;
import TaskSphere.demo.entity.TaskStatus;
import TaskSphere.demo.entity.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TaskRequest {
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private LocalDateTime deadline;
    @NotNull
    private TaskPriority priority;
    private TaskStatus status;
    @NotNull
    private TaskType taskType;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }
    public TaskPriority getPriority() { return priority; }
    public void setPriority(TaskPriority priority) { this.priority = priority; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public TaskType getTaskType() { return taskType; }
    public void setTaskType(TaskType taskType) { this.taskType = taskType; }
}
