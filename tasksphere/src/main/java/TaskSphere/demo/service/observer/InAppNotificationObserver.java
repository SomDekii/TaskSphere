package TaskSphere.demo.service.observer;

import TaskSphere.demo.entity.Notification;
import TaskSphere.demo.entity.NotificationType;
import TaskSphere.demo.repository.NotificationRepository;
import TaskSphere.demo.service.NotificationManager;
import TaskSphere.demo.service.adapter.InAppAdapter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InAppNotificationObserver implements Observer {
    private final NotificationRepository notificationRepository;
    private final NotificationManager notificationManager;
    private final InAppAdapter inAppAdapter;

    public InAppNotificationObserver(NotificationRepository notificationRepository,
                                     NotificationManager notificationManager,
                                     InAppAdapter inAppAdapter) {
        this.notificationRepository = notificationRepository;
        this.notificationManager = notificationManager;
        this.inAppAdapter = inAppAdapter;
    }

    @Override
    public void update(String userId, String taskId, String message, NotificationType type) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setType(type);
        notification.setReadStatus(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUserId(userId);
        notification.setTaskId(taskId);
        Notification saved = notificationRepository.save(notification);
        inAppAdapter.send(userId, message);
        notificationManager.publish(userId, saved);
    }
}
