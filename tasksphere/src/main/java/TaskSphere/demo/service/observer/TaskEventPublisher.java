package TaskSphere.demo.service.observer;

import TaskSphere.demo.entity.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class TaskEventPublisher {
    private final NotificationSubject notificationSubject;

    public TaskEventPublisher(NotificationSubject notificationSubject) {
        this.notificationSubject = notificationSubject;
    }

    // Observer Pattern: task lifecycle changes are published as events and observers decide how to react.
    public void taskCreated(String userId, String taskId, String message) {
        publish(userId, taskId, message, NotificationType.TASK_CREATED);
    }

    public void taskStatusUpdated(String userId, String taskId, String message, NotificationType type) {
        publish(userId, taskId, message, type);
    }

    public void taskAssigned(String userId, String taskId, String message) {
        publish(userId, taskId, message, NotificationType.TASK_ASSIGNED);
    }

    public void deadlineApproaching(String userId, String taskId, String message) {
        publish(userId, taskId, message, NotificationType.DEADLINE);
    }

    private void publish(String userId, String taskId, String message, NotificationType type) {
        notificationSubject.notifyObservers(new TaskEvent(userId, taskId, message, type));
    }
}
