package TaskSphere.demo.service.strategy;

import TaskSphere.demo.entity.Task;
import TaskSphere.demo.entity.TaskStatus;
import TaskSphere.demo.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatusFilterStrategy implements TaskFilterStrategy {
    @Override
    public boolean supports(String key) {
        return "status".equalsIgnoreCase(key);
    }

    @Override
    public List<Task> filter(List<Task> tasks, String value) {
        TaskStatus selectedStatus = parseStatus(value);
        return tasks.stream()
                .filter(task -> task.getStatus() == selectedStatus)
                .toList();
    }

    private TaskStatus parseStatus(String value) {
        try {
            return TaskStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid status filter");
        }
    }
}
