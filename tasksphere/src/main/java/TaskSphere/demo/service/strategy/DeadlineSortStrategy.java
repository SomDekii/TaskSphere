package TaskSphere.demo.service.strategy;

import TaskSphere.demo.entity.Task;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DeadlineSortStrategy implements TaskStrategy, TaskSortStrategy {
    @Override
    public boolean supports(String key) {
        return "deadline".equalsIgnoreCase(key);
    }

    @Override
    public List<Task> apply(List<Task> tasks) {
        return sort(tasks);
    }

    @Override
    public List<Task> sort(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getDeadline, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }
}
