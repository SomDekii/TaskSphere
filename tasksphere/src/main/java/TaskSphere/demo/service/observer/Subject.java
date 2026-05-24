package TaskSphere.demo.service.observer;

import TaskSphere.demo.entity.NotificationType;

public interface Subject {
    void register(Observer observer);
    void unregister(Observer observer);
    void notifyObservers(TaskEvent event);

    default void notifyObservers(String userId, String taskId, String message, NotificationType type) {
        notifyObservers(new TaskEvent(userId, taskId, message, type));
    }
}
