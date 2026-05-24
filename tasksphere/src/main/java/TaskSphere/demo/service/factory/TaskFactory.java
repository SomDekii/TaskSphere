package TaskSphere.demo.service.factory;

import TaskSphere.demo.dto.TaskRequest;
import TaskSphere.demo.entity.Task;
import TaskSphere.demo.entity.TaskStatus;
import TaskSphere.demo.entity.TaskType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskFactory {
    // Factory Pattern: centralizes task object creation while preserving the existing Task entity and DTO contract.
    public Task createTask(TaskRequest request, String userId, LocalDateTime now) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDeadline(request.getDeadline());
        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus() == null ? TaskStatus.PENDING : request.getStatus());
        task.setTaskType(request.getTaskType());
        task.setUserId(userId);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        return task;
    }

    public TaskBehavior create(TaskType type) {
        return switch (type) {
            case WORK -> new WorkTask();
            case STUDY -> new StudyTask();
            case PERSONAL -> new PersonalTask();
        };
    }
}
