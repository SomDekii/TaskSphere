package TaskSphere.demo.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionManager {
    private final MongoClient mongoClient;
    private final String databaseName;

    public DatabaseConnectionManager(MongoClient mongoClient,
                                     @Value("${spring.data.mongodb.database}") String databaseName) {
        this.mongoClient = mongoClient;
        this.databaseName = databaseName;
    }

    // Singleton Pattern: Spring creates this component and MongoClient once per app lifecycle.
    public MongoDatabase database() {
        return mongoClient.getDatabase(databaseName);
    }
}
