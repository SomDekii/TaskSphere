package TaskSphere.demo.service.strategy;

import TaskSphere.demo.entity.Task;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PrioritySortStrategy implements TaskStrategy, TaskSortStrategy {
    @Override
    public boolean supports(String key) {
        return "priority".equalsIgnoreCase(key);
    }

    @Override
    public List<Task> apply(List<Task> tasks) {
        return sort(tasks);
    }

    @Override
    public List<Task> sort(List<Task> tasks) {
        return tasks.stream()
                .sorted((first, second) -> Integer.compare(priorityRank(second), priorityRank(first)))
                .toList();
    }

    private int priorityRank(Task task) {
        if (task.getPriority() == null) {
            return 0;
        }
        return switch (task.getPriority()) {
            case LOW -> 1;
            case MEDIUM -> 2;
            case HIGH -> 3;
        };
    }
}
