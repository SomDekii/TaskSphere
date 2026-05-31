package TaskSphere.demo.service.observer;

import TaskSphere.demo.entity.Notification;
import TaskSphere.demo.entity.NotificationType;
import TaskSphere.demo.repository.NotificationRepository;
import TaskSphere.demo.service.NotificationManager;
import TaskSphere.demo.service.adapter.NotificationAdapterRegistry;
import TaskSphere.demo.service.adapter.NotificationChannel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InAppNotificationObserver implements Observer {
    private final NotificationRepository notificationRepository;
    private final NotificationManager notificationManager;
    private final NotificationAdapterRegistry notificationAdapterRegistry;

    public InAppNotificationObserver(NotificationRepository notificationRepository,
                                     NotificationManager notificationManager,
                                     NotificationAdapterRegistry notificationAdapterRegistry) {
        this.notificationRepository = notificationRepository;
        this.notificationManager = notificationManager;
        this.notificationAdapterRegistry = notificationAdapterRegistry;
    }

    @Override
    public void update(TaskEvent event) {
        Notification notification = new Notification();
        notification.setMessage(event.getMessage());
        notification.setType(event.getType());
        notification.setReadStatus(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUserId(event.getUserId());
        notification.setTaskId(event.getTaskId());
        Notification saved = notificationRepository.save(notification);
        notificationAdapterRegistry.send(NotificationChannel.IN_APP, event.getUserId(), event.getMessage());
        notificationManager.publish(event.getUserId(), saved);
    }
}
