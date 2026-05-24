package TaskSphere.demo.service.strategy;

import TaskSphere.demo.entity.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssigneeFilterStrategy implements TaskFilterStrategy {
    @Override
    public boolean supports(String key) {
        return "assignee".equalsIgnoreCase(key);
    }

    @Override
    public List<Task> filter(List<Task> tasks, String value) {
        // Backward-compatible assignee support: current data model stores the assigned owner in userId.
        return tasks.stream()
                .filter(task -> value.equals(task.getUserId()))
                .toList();
    }
}
