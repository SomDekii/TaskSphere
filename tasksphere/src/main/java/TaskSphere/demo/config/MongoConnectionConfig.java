package TaskSphere.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MongoConnectionConfig {

    @Bean
    public MongoClientSettingsBuilderCustomizer mongoClientSettingsBuilderCustomizer(
            @Value("${app.mongodb.server-selection-timeout-ms:10000}") int serverSelectionTimeoutMs,
            @Value("${app.mongodb.connect-timeout-ms:10000}") int connectTimeoutMs,
            @Value("${app.mongodb.read-timeout-ms:15000}") int readTimeoutMs) {
        return builder -> builder
                .applyToClusterSettings(settings ->
                        settings.serverSelectionTimeout(serverSelectionTimeoutMs, TimeUnit.MILLISECONDS))
                .applyToSocketSettings(settings -> settings
                        .connectTimeout(connectTimeoutMs, TimeUnit.MILLISECONDS)
                        .readTimeout(readTimeoutMs, TimeUnit.MILLISECONDS));
    }
}
