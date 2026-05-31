package TaskSphere.demo.service.observer;

import TaskSphere.demo.entity.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class NotificationSubject implements Subject {
    private static final Logger logger = LoggerFactory.getLogger(NotificationSubject.class);
    private final List<Observer> observers = new CopyOnWriteArrayList<>();

    public NotificationSubject(List<Observer> observers) {
        this.observers.addAll(observers);
    }

    @Override
    public void register(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(TaskEvent event) {
        for (Observer observer : observers) {
            try {
                observer.update(event);
            } catch (RuntimeException ex) {
                logger.warn("Notification observer {} failed for task {}", observer.getClass().getSimpleName(), event.getTaskId(), ex);
            }
        }
    }

    @Override
    public void notifyObservers(String userId, String taskId, String message, NotificationType type) {
        notifyObservers(new TaskEvent(userId, taskId, message, type));
    }
}
