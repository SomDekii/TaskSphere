package TaskSphere.demo.repository;

import TaskSphere.demo.entity.Task;
import TaskSphere.demo.entity.TaskStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByUserId(String userId);
    Optional<Task> findByIdAndUserId(String id, String userId);
    List<Task> findByUserIdAndTitleContainingIgnoreCase(String userId, String title);
    long countByUserId(String userId);
    long countByUserIdAndStatus(String userId, TaskStatus status);
    List<Task> findTop5ByUserIdAndDeadlineAfterOrderByDeadlineAsc(String userId, LocalDateTime now);
    List<Task> findByDeadlineBetweenAndStatusNot(LocalDateTime start, LocalDateTime end, TaskStatus status);
}
