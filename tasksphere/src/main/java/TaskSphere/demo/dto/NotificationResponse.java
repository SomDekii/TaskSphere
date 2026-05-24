package TaskSphere.demo.dto;

import TaskSphere.demo.entity.Notification;
import TaskSphere.demo.entity.NotificationType;

import java.time.LocalDateTime;

public class NotificationResponse {
    private String id;
    private String message;
    private NotificationType type;
    private boolean readStatus;
    private LocalDateTime createdAt;
    private String taskId;

    public static NotificationResponse from(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.id = notification.getId();
        response.message = notification.getMessage();
        response.type = notification.getType();
        response.readStatus = notification.isReadStatus();
        response.createdAt = notification.getCreatedAt();
        response.taskId = notification.getTaskId();
        return response;
    }

    public String getId() { return id; }
    public String getMessage() { return message; }
    public NotificationType getType() { return type; }
    public boolean isReadStatus() { return readStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getTaskId() { return taskId; }
}
