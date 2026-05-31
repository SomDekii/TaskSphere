package TaskSphere.demo.service.strategy;

import TaskSphere.demo.entity.Task;

import java.util.List;

public interface TaskFilterStrategy {
    boolean supports(String key);
    List<Task> filter(List<Task> tasks, String value);
}
