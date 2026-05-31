package TaskSphere.demo.service;

import TaskSphere.demo.entity.TaskStatus;
import TaskSphere.demo.repository.TaskRepository;
import TaskSphere.demo.service.observer.TaskEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeadlineReminderService {
    private static final Logger logger = LoggerFactory.getLogger(DeadlineReminderService.class);

    private final TaskRepository taskRepository;
    private final TaskEventPublisher taskEventPublisher;

    public DeadlineReminderService(TaskRepository taskRepository,
                                   TaskEventPublisher taskEventPublisher) {
        this.taskRepository = taskRepository;
        this.taskEventPublisher = taskEventPublisher;
    }

    @Scheduled(
            initialDelayString = "${app.deadline-reminders.initial-delay-ms:60000}",
            fixedDelayString = "${app.deadline-reminders.fixed-delay-ms:300000}"
    )
    public void sendDeadlineReminders() {
        try {
            LocalDateTime now = LocalDateTime.now();
            taskRepository.findByDeadlineBetweenAndStatusNot(now, now.plusHours(24), TaskStatus.COMPLETED)
                    .forEach(task -> taskEventPublisher.deadlineApproaching(
                            task.getUserId(),
                            task.getId(),
                            "Deadline approaching: " + task.getTitle()
                    ));
        } catch (DataAccessResourceFailureException ex) {
            logger.warn("Skipping deadline reminder scan because MongoDB is unavailable: {}", ex.getMostSpecificCause().getMessage());
            logger.debug("MongoDB failure while scanning deadline reminders", ex);
        }
    }
}
