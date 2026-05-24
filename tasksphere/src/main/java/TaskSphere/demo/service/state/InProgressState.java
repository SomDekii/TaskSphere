package TaskSphere.demo.service.state;

import TaskSphere.demo.entity.TaskStatus;

public class InProgressState implements TaskState {
    @Override
    public boolean canMoveTo(TaskStatus nextStatus) {
        return nextStatus == TaskStatus.IN_PROGRESS || nextStatus == TaskStatus.COMPLETED;
    }
}
