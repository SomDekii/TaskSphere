package TaskSphere.demo.service.state;

import TaskSphere.demo.entity.TaskStatus;

public class CompletedState implements TaskState {
    @Override
    public boolean canMoveTo(TaskStatus nextStatus) {
        return nextStatus == TaskStatus.COMPLETED;
    }
}
