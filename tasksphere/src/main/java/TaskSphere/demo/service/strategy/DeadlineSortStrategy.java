package TaskSphere.demo.service.strategy;

import TaskSphere.demo.entity.Task;

import java.util.Comparator;
import java.util.List;

public class DeadlineSortStrategy implements TaskStrategy {
    @Override
    public List<Task> apply(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getDeadline))
                .toList();
    }
}
