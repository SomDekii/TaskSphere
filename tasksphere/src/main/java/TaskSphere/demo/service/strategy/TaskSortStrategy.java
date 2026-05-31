package TaskSphere.demo.service.strategy;

import TaskSphere.demo.entity.Task;

import java.util.List;

public interface TaskSortStrategy {
    boolean supports(String key);
    List<Task> sort(List<Task> tasks);
}
