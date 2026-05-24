package TaskSphere.demo.service.observer;

import TaskSphere.demo.entity.NotificationType;
import TaskSphere.demo.repository.UserRepository;
import TaskSphere.demo.service.adapter.EmailAdapter;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationObserver implements Observer {
    private final EmailAdapter emailAdapter;
    private final UserRepository userRepository;

    public EmailNotificationObserver(EmailAdapter emailAdapter, UserRepository userRepository) {
        this.emailAdapter = emailAdapter;
        this.userRepository = userRepository;
    }

    @Override
    public void update(String userId, String taskId, String message, NotificationType type) {
        userRepository.findById(userId).ifPresent(user -> emailAdapter.send(user.getEmail(), message));
    }
}
