package TaskSphere.demo;

import TaskSphere.demo.dto.TaskRequest;
import TaskSphere.demo.entity.Task;
import TaskSphere.demo.entity.TaskPriority;
import TaskSphere.demo.entity.TaskStatus;
import TaskSphere.demo.entity.TaskType;
import TaskSphere.demo.service.factory.TaskFactory;
import TaskSphere.demo.service.state.TaskStateFactory;
import TaskSphere.demo.service.strategy.DeadlineSortStrategy;
import TaskSphere.demo.service.strategy.PriorityFilterStrategy;
import TaskSphere.demo.service.strategy.StatusFilterStrategy;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatternCompatibilityTests {
    @Test
    void factoryCreatesTaskWithExistingDefaults() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Write report");
        request.setDescription("Draft monthly summary");
        request.setDeadline(LocalDateTime.now().plusDays(1));
        request.setPriority(TaskPriority.HIGH);
        request.setTaskType(TaskType.WORK);

        Task task = new TaskFactory().createTask(request, "user-1", LocalDateTime.now());

        assertEquals("Write report", task.getTitle());
        assertEquals(TaskStatus.PENDING, task.getStatus());
        assertEquals("user-1", task.getUserId());
    }

    @Test
    void statePreventsInvalidBackwardTransition() {
        TaskStateFactory factory = new TaskStateFactory();

        assertTrue(factory.from(TaskStatus.PENDING).canMoveTo(TaskStatus.IN_PROGRESS));
        assertFalse(factory.from(TaskStatus.COMPLETED).canMoveTo(TaskStatus.IN_PROGRESS));
        assertTrue(factory.from(TaskStatus.COMPLETED).canMoveTo(TaskStatus.ARCHIVED));
    }

    @Test
    void strategiesKeepExistingStatusPriorityAndDeadlineBehavior() {
        Task first = task("first", TaskStatus.PENDING, TaskPriority.LOW, LocalDateTime.now().plusDays(2));
        Task second = task("second", TaskStatus.COMPLETED, TaskPriority.HIGH, LocalDateTime.now().plusDays(1));

        List<Task> pending = new StatusFilterStrategy().filter(List.of(first, second), "PENDING");
        List<Task> highPriority = new PriorityFilterStrategy().filter(List.of(first, second), "HIGH");
        List<Task> byDeadline = new DeadlineSortStrategy().sort(List.of(first, second));

        assertEquals(List.of(first), pending);
        assertEquals(List.of(second), highPriority);
        assertEquals(List.of(second, first), byDeadline);
    }

    private Task task(String title, TaskStatus status, TaskPriority priority, LocalDateTime deadline) {
        Task task = new Task();
        task.setTitle(title);
        task.setStatus(status);
        task.setPriority(priority);
        task.setDeadline(deadline);
        task.setTaskType(TaskType.PERSONAL);
        task.setUserId("user-1");
        return task;
    }
}
