package TaskSphere.demo.service.impl;

import TaskSphere.demo.dto.DashboardResponse;
import TaskSphere.demo.dto.NotificationResponse;
import TaskSphere.demo.dto.TaskResponse;
import TaskSphere.demo.entity.TaskStatus;
import TaskSphere.demo.repository.NotificationRepository;
import TaskSphere.demo.repository.TaskRepository;
import TaskSphere.demo.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final TaskRepository taskRepository;
    private final NotificationRepository notificationRepository;

    public DashboardServiceImpl(TaskRepository taskRepository,
                                NotificationRepository notificationRepository) {
        this.taskRepository = taskRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public DashboardResponse getDashboard(String userId) {
        return new DashboardResponse(
                taskRepository.countByUserId(userId),
                taskRepository.countByUserIdAndStatus(userId, TaskStatus.COMPLETED),
                taskRepository.countByUserIdAndStatus(userId, TaskStatus.PENDING),
                taskRepository.countByUserIdAndStatus(userId, TaskStatus.IN_PROGRESS),
                taskRepository.findTop5ByUserIdAndDeadlineAfterOrderByDeadlineAsc(userId, LocalDateTime.now())
                        .stream().map(TaskResponse::from).toList(),
                notificationRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId)
                        .stream().map(NotificationResponse::from).toList()
        );
    }
}
