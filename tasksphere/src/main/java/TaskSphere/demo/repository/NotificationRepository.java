package TaskSphere.demo.repository;

import TaskSphere.demo.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
    List<Notification> findTop5ByUserIdOrderByCreatedAtDesc(String userId);
    Optional<Notification> findByIdAndUserId(String id, String userId);
}
