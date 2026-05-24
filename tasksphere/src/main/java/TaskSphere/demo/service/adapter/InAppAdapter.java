package TaskSphere.demo.service.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InAppAdapter implements NotificationAdapter {
    private static final Logger logger = LoggerFactory.getLogger(InAppAdapter.class);

    @Override
    public void send(String recipient, String message) {
        logger.info("In-app notification for {}: {}", recipient, message);
    }
}
