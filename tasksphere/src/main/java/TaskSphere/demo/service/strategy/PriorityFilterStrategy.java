package TaskSphere.demo.service.strategy;

import TaskSphere.demo.entity.Task;
import TaskSphere.demo.entity.TaskPriority;
import TaskSphere.demo.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PriorityFilterStrategy implements TaskFilterStrategy {
    @Override
    public boolean supports(String key) {
        return "priority".equalsIgnoreCase(key);
    }

    @Override
    public List<Task> filter(List<Task> tasks, String value) {
        TaskPriority selectedPriority = parsePriority(value);
        return tasks.stream()
                .filter(task -> task.getPriority() == selectedPriority)
                .toList();
    }

    private TaskPriority parsePriority(String value) {
        try {
            return TaskPriority.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid priority filter");
        }
    }
}
