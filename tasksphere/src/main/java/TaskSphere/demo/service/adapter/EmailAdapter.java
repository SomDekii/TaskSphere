package TaskSphere.demo.service.adapter;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailAdapter implements NotificationAdapter {
    private static final Logger logger = LoggerFactory.getLogger(EmailAdapter.class);
    private final JavaMailSender mailSender;
    private final boolean emailEnabled;
    private final String fromAddress;

    public EmailAdapter(ObjectProvider<JavaMailSender> mailSender,
                        @Value("${app.notifications.email.enabled:false}") boolean emailEnabled,
                        @Value("${app.notifications.email.from:no-reply@tasksphere.local}") String fromAddress) {
        this.mailSender = mailSender.getIfAvailable();
        this.emailEnabled = emailEnabled;
        this.fromAddress = fromAddress;
    }

    @Override
    public void send(String recipient, String message) {
        if (!emailEnabled) {
            logger.info("Email notifications disabled. Would send to {}: {}", recipient, message);
            return;
        }
        if (mailSender == null) {
            logger.warn("Email notifications enabled, but no JavaMailSender is configured.");
            return;
        }
        if (recipient == null || recipient.isBlank()) {
            logger.warn("Email notification skipped because recipient is empty.");
            return;
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromAddress);
        mailMessage.setTo(recipient);
        mailMessage.setSubject("TaskSphere notification");
        mailMessage.setText(message);
        mailSender.send(mailMessage);
        logger.info("Email notification sent to {}", recipient);
    }
}
