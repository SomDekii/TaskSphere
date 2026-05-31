package TaskSphere.demo.service.impl;

import TaskSphere.demo.dto.NotificationResponse;
import TaskSphere.demo.entity.Notification;
import TaskSphere.demo.entity.User;
import TaskSphere.demo.exception.ResourceNotFoundException;
import TaskSphere.demo.repository.NotificationRepository;
import TaskSphere.demo.repository.UserRepository;
import TaskSphere.demo.service.NotificationManager;
import TaskSphere.demo.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationManager notificationManager;
    private final UserRepository userRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   NotificationManager notificationManager,
                                   UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.notificationManager = notificationManager;
        this.userRepository = userRepository;
    }

    @Override
    public List<NotificationResponse> getNotifications(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Override
    public NotificationResponse markRead(String userId, String id) {
        Notification notification = findUserNotification(userId, id);
        notification.setReadStatus(true);
        return NotificationResponse.from(notificationRepository.save(notification));
    }

    @Override
    public void delete(String userId, String id) {
        notificationRepository.delete(findUserNotification(userId, id));
    }

    @Override
    public void registerPushToken(String userId, String pushToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setPushToken(pushToken);
        userRepository.save(user);
    }

    @Override
    public SseEmitter stream(String userId) {
        return notificationManager.subscribe(userId);
    }

    private Notification findUserNotification(String userId, String id) {
        return notificationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
    }
}
