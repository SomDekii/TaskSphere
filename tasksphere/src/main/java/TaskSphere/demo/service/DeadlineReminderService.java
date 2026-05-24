package TaskSphere.demo.service;

import TaskSphere.demo.entity.NotificationType;
import TaskSphere.demo.entity.TaskStatus;
import TaskSphere.demo.repository.TaskRepository;
import TaskSphere.demo.service.observer.TaskEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeadlineReminderService {
    private final TaskRepository taskRepository;
    private final TaskEventPublisher taskEventPublisher;

    public DeadlineReminderService(TaskRepository taskRepository,
                                   TaskEventPublisher taskEventPublisher) {
        this.taskRepository = taskRepository;
        this.taskEventPublisher = taskEventPublisher;
    }

    @Scheduled(fixedRate = 300000)
    public void sendDeadlineReminders() {
        LocalDateTime now = LocalDateTime.now();
        taskRepository.findByDeadlineBetweenAndStatusNot(now, now.plusHours(24), TaskStatus.COMPLETED)
                .forEach(task -> taskEventPublisher.deadlineApproaching(
                        task.getUserId(),
                        task.getId(),
                        "Deadline approaching: " + task.getTitle()
                ));
    }
}
