package TaskSphere.demo.service.adapter;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class NotificationAdapterRegistry {
    private final Map<NotificationChannel, NotificationAdapter> adapters = new EnumMap<>(NotificationChannel.class);

    public NotificationAdapterRegistry(EmailAdapter emailAdapter,
                                       SMSAdapter smsAdapter,
                                       PushAdapter pushAdapter,
                                       InAppAdapter inAppAdapter) {
        adapters.put(NotificationChannel.EMAIL, emailAdapter);
        adapters.put(NotificationChannel.SMS, smsAdapter);
        adapters.put(NotificationChannel.PUSH, pushAdapter);
        adapters.put(NotificationChannel.IN_APP, inAppAdapter);
    }

    // Adapter Pattern: business code selects a channel and stays independent from provider-specific details.
    public void send(NotificationChannel channel, String recipient, String message) {
        NotificationAdapter adapter = adapters.get(channel);
        if (adapter == null) {
            throw new IllegalArgumentException("Unsupported notification channel: " + channel);
        }
        adapter.send(recipient, message);
    }
}
