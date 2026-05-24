package TaskSphere.demo.service.state;

import TaskSphere.demo.entity.TaskStatus;

public class PendingState implements TaskState {
    @Override
    public boolean canMoveTo(TaskStatus nextStatus) {
        return nextStatus == TaskStatus.PENDING || nextStatus == TaskStatus.IN_PROGRESS;
    }
}
