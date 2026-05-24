package TaskSphere.demo.service.observer;

import TaskSphere.demo.entity.NotificationType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class NotificationSubject implements Subject {
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
    public void notifyObservers(String userId, String taskId, String message, NotificationType type) {
        observers.forEach(observer -> observer.update(userId, taskId, message, type));
    }
}
