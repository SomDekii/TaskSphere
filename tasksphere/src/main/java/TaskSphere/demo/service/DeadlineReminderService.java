package TaskSphere.demo.service;

import TaskSphere.demo.entity.NotificationType;
import TaskSphere.demo.entity.TaskStatus;
import TaskSphere.demo.repository.TaskRepository;
import TaskSphere.demo.service.observer.NotificationSubject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeadlineReminderService {
    private final TaskRepository taskRepository;
    private final NotificationSubject notificationSubject;

    public DeadlineReminderService(TaskRepository taskRepository,
                                   NotificationSubject notificationSubject) {
        this.taskRepository = taskRepository;
        this.notificationSubject = notificationSubject;
    }

    @Scheduled(fixedRate = 300000)
    public void sendDeadlineReminders() {
        LocalDateTime now = LocalDateTime.now();
        taskRepository.findByDeadlineBetweenAndStatusNot(now, now.plusHours(24), TaskStatus.COMPLETED)
                .forEach(task -> notificationSubject.notifyObservers(
                        task.getUserId(),
                        task.getId(),
                        "Deadline approaching: " + task.getTitle(),
                        NotificationType.DEADLINE
                ));
    }
}
