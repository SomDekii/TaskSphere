package TaskSphere.demo.service.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailAdapter implements NotificationAdapter {
    private static final Logger logger = LoggerFactory.getLogger(EmailAdapter.class);

    @Override
    public void send(String recipient, String message) {
        logger.info("Mock email to {}: {}", recipient, message);
    }
}
