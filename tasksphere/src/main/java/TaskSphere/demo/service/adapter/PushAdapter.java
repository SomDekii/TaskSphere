package TaskSphere.demo.service.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class PushAdapter implements NotificationAdapter {
    private static final Logger logger = LoggerFactory.getLogger(PushAdapter.class);
    private final HttpClient httpClient;
    private final boolean pushEnabled;
    private final String endpoint;
    private final String authToken;

    public PushAdapter(@Value("${app.notifications.push.enabled:false}") boolean pushEnabled,
                       @Value("${app.notifications.push.endpoint:}") String endpoint,
                       @Value("${app.notifications.push.auth-token:}") String authToken) {
        this.httpClient = HttpClient.newHttpClient();
        this.pushEnabled = pushEnabled;
        this.endpoint = endpoint;
        this.authToken = authToken;
    }

    @Override
    public void send(String recipient, String message) {
        if (!pushEnabled) {
            logger.info("Push notifications disabled. Would send to {}: {}", recipient, message);
            return;
        }
        if (endpoint == null || endpoint.isBlank()) {
            logger.warn("Push notifications enabled, but no endpoint is configured.");
            return;
        }
        if (recipient == null || recipient.isBlank()) {
            logger.warn("Push notification skipped because recipient token is empty.");
            return;
        }

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload(recipient, message)));
        if (authToken != null && !authToken.isBlank()) {
            requestBuilder.header("Authorization", "Bearer " + authToken);
        }

        try {
            HttpResponse<String> response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                logger.warn("Push notification failed with status {}: {}", response.statusCode(), response.body());
                return;
            }
            logger.info("Push notification sent to {}", recipient);
        } catch (IOException ex) {
            throw new IllegalStateException("Push notification delivery failed", ex);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Push notification delivery interrupted", ex);
        }
    }

    private String payload(String recipient, String message) {
        return "{\"recipient\":\"" + escapeJson(recipient) + "\",\"message\":\"" + escapeJson(message) + "\"}";
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
