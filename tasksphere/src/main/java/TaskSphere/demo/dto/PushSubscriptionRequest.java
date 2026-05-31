package TaskSphere.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class PushSubscriptionRequest {
    @NotBlank
    private String pushToken;

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken == null ? null : pushToken.trim();
    }
}
