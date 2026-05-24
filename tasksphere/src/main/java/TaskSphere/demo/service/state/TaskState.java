package TaskSphere.demo.service.state;

import TaskSphere.demo.entity.TaskStatus;

public interface TaskState {
    boolean canMoveTo(TaskStatus nextStatus);
}
