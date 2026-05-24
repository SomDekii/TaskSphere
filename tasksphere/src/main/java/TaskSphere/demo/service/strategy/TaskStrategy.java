package TaskSphere.demo.service.strategy;

import TaskSphere.demo.entity.Task;

import java.util.List;

public interface TaskStrategy {
    List<Task> apply(List<Task> tasks);
}
