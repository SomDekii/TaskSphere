package TaskSphere.demo.service.strategy;

import TaskSphere.demo.entity.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskStrategyRegistry {
    private final List<TaskFilterStrategy> filterStrategies;
    private final List<TaskSortStrategy> sortStrategies;

    public TaskStrategyRegistry(List<TaskFilterStrategy> filterStrategies,
                                List<TaskSortStrategy> sortStrategies) {
        this.filterStrategies = filterStrategies;
        this.sortStrategies = sortStrategies;
    }

    // Strategy Pattern: callers ask for a filter/sort by key without depending on concrete implementations.
    public List<Task> applyFilter(String key, List<Task> tasks, String value) {
        if (!hasText(value)) {
            return tasks;
        }
        return findFilter(key)
                .map(strategy -> strategy.filter(tasks, value))
                .orElse(tasks);
    }

    public List<Task> applySort(String key, List<Task> tasks) {
        if (!hasText(key)) {
            return tasks;
        }
        return sortStrategies.stream()
                .filter(strategy -> strategy.supports(key))
                .findFirst()
                .map(strategy -> strategy.sort(tasks))
                .orElse(tasks);
    }

    private Optional<TaskFilterStrategy> findFilter(String key) {
        return filterStrategies.stream()
                .filter(strategy -> strategy.supports(key))
                .findFirst();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
