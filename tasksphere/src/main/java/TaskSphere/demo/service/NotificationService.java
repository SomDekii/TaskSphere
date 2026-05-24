package TaskSphere.demo.service;

import TaskSphere.demo.dto.NotificationResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getNotifications(String userId);
    NotificationResponse markRead(String userId, String id);
    void delete(String userId, String id);
    SseEmitter stream(String userId);
}
