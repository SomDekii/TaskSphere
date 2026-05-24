package TaskSphere.demo.service.observer;

import TaskSphere.demo.entity.NotificationType;

public class TaskEvent {
    private final String userId;
    private final String taskId;
    private final String message;
    private final NotificationType type;

    public TaskEvent(String userId, String taskId, String message, NotificationType type) {
        this.userId = userId;
        this.taskId = taskId;
        this.message = message;
        this.type = type;
    }

    public String getUserId() { return userId; }
    public String getTaskId() { return taskId; }
    public String getMessage() { return message; }
    public NotificationType getType() { return type; }
}
