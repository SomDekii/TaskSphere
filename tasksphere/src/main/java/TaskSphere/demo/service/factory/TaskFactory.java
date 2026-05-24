package TaskSphere.demo.service.factory;

import TaskSphere.demo.entity.TaskType;
import org.springframework.stereotype.Component;

@Component
public class TaskFactory {
    public TaskBehavior create(TaskType type) {
        return switch (type) {
            case WORK -> new WorkTask();
            case STUDY -> new StudyTask();
            case PERSONAL -> new PersonalTask();
        };
    }
}
