package TaskSphere.demo.service.observer;

import TaskSphere.demo.entity.NotificationType;

public interface Observer {
    void update(TaskEvent event);

    default void update(String userId, String taskId, String message, NotificationType type) {
        update(new TaskEvent(userId, taskId, message, type));
    }
}
