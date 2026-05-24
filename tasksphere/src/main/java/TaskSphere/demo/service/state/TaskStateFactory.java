package TaskSphere.demo.service.state;

import TaskSphere.demo.entity.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskStateFactory {
    // State Pattern: resolves the behavior object for the task's current status.
    public TaskState from(TaskStatus status) {
        return switch (status) {
            case PENDING -> new PendingState();
            case IN_PROGRESS -> new InProgressState();
            case COMPLETED -> new CompletedState();
            case ARCHIVED -> new ArchivedState();
        };
    }
}
