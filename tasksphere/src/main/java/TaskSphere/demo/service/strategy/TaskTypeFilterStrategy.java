package TaskSphere.demo.service.strategy;

import TaskSphere.demo.entity.Task;
import TaskSphere.demo.entity.TaskType;
import TaskSphere.demo.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskTypeFilterStrategy implements TaskFilterStrategy {
    @Override
    public boolean supports(String key) {
        return "type".equalsIgnoreCase(key);
    }

    @Override
    public List<Task> filter(List<Task> tasks, String value) {
        TaskType selectedType = parseType(value);
        return tasks.stream()
                .filter(task -> task.getTaskType() == selectedType)
                .toList();
    }

    private TaskType parseType(String value) {
        try {
            return TaskType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid type filter");
        }
    }
}
