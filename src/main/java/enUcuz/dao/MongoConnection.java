package enUcuz.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import enUcuz.util.ProjectSettings;
import org.bson.Document;

public class MongoConnection {

    public MongoCollection<Document> repository;
    private static MongoConnection instance = new MongoConnection();

    // won't support multi threading
    public static MongoConnection getInstance(){
        return (instance == null) ? (new MongoConnection()) : instance;
    }

    private MongoConnection() {
        ProjectSettings settings = ProjectSettings.getInstance();

        String uri = (String)settings.Get("uri");
        String dbName = (String)settings.Get("database");
        String collectionName = (String)settings.Get("collection");

        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        this.repository = database.getCollection(collectionName);
    }

}
