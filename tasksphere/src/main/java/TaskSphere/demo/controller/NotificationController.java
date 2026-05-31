package TaskSphere.demo.controller;

import TaskSphere.demo.dto.NotificationResponse;
import TaskSphere.demo.dto.PushSubscriptionRequest;
import TaskSphere.demo.entity.User;
import TaskSphere.demo.service.CurrentUserService;
import TaskSphere.demo.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final CurrentUserService currentUserService;

    public NotificationController(NotificationService notificationService, CurrentUserService currentUserService) {
        this.notificationService = notificationService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<NotificationResponse> getNotifications(Authentication authentication) {
        User user = currentUserService.getUser(authentication);
        return notificationService.getNotifications(user.getId());
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(Authentication authentication) {
        User user = currentUserService.getUser(authentication);
        return notificationService.stream(user.getId());
    }

    @PostMapping("/push-subscription")
    public ResponseEntity<Void> registerPushSubscription(Authentication authentication,
                                                        @Valid @RequestBody PushSubscriptionRequest request) {
        User user = currentUserService.getUser(authentication);
        notificationService.registerPushToken(user.getId(), request.getPushToken());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/read")
    public NotificationResponse markRead(Authentication authentication, @PathVariable String id) {
        User user = currentUserService.getUser(authentication);
        return notificationService.markRead(user.getId(), id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable String id) {
        User user = currentUserService.getUser(authentication);
        notificationService.delete(user.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
