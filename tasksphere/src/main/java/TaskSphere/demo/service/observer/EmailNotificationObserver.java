package TaskSphere.demo.service.observer;

import TaskSphere.demo.entity.NotificationType;
import TaskSphere.demo.repository.UserRepository;
import TaskSphere.demo.service.adapter.NotificationAdapterRegistry;
import TaskSphere.demo.service.adapter.NotificationChannel;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationObserver implements Observer {
    private final NotificationAdapterRegistry notificationAdapterRegistry;
    private final UserRepository userRepository;

    public EmailNotificationObserver(NotificationAdapterRegistry notificationAdapterRegistry, UserRepository userRepository) {
        this.notificationAdapterRegistry = notificationAdapterRegistry;
        this.userRepository = userRepository;
    }

    @Override
    public void update(TaskEvent event) {
        userRepository.findById(event.getUserId())
                .ifPresent(user -> notificationAdapterRegistry.send(NotificationChannel.EMAIL, user.getEmail(), event.getMessage()));
    }
}
