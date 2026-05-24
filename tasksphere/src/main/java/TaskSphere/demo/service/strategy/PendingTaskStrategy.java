package TaskSphere.demo.service.strategy;

import TaskSphere.demo.entity.Task;
import TaskSphere.demo.entity.TaskStatus;

import java.util.List;

public class PendingTaskStrategy implements TaskStrategy {
    @Override
    public List<Task> apply(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.PENDING)
                .toList();
    }
}
