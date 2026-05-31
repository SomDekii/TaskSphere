package TaskSphere.demo.service.observer;

import TaskSphere.demo.repository.UserRepository;
import TaskSphere.demo.service.adapter.NotificationAdapterRegistry;
import TaskSphere.demo.service.adapter.NotificationChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PushNotificationObserver implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(PushNotificationObserver.class);
    private final NotificationAdapterRegistry notificationAdapterRegistry;
    private final UserRepository userRepository;

    public PushNotificationObserver(NotificationAdapterRegistry notificationAdapterRegistry, UserRepository userRepository) {
        this.notificationAdapterRegistry = notificationAdapterRegistry;
        this.userRepository = userRepository;
    }

    @Override
    public void update(TaskEvent event) {
        userRepository.findById(event.getUserId()).ifPresent(user -> {
            String pushToken = user.getPushToken();
            if (pushToken == null || pushToken.isBlank()) {
                logger.info("Push notification skipped for user {} because no push token is stored.", user.getId());
                return;
            }
            notificationAdapterRegistry.send(NotificationChannel.PUSH, pushToken, event.getMessage());
        });
    }
}
