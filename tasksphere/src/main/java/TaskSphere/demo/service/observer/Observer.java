package TaskSphere.demo.service.observer;

import TaskSphere.demo.entity.NotificationType;

public interface Observer {
    void update(String userId, String taskId, String message, NotificationType type);
}
