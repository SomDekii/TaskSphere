package TaskSphere.demo.service.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SMSAdapter implements NotificationAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SMSAdapter.class);

    @Override
    public void send(String recipient, String message) {
        logger.info("Mock SMS to {}: {}", recipient, message);
    }
}
