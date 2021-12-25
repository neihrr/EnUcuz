package enUcuz.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import enUcuz.dao.MongoConnection;
import enUcuz.interfaces.IDocument;
import enUcuz.model.AbstractDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.regex.Pattern;

public class MongoRepository<T extends AbstractDocument>{

    public enum By{
        NAME,
        PRICE,
        CATEGORY,
        MARKET
    }

    private MongoCollection repository = MongoConnection.getInstance().repository;
    private static MongoRepository instance = new MongoRepository();

    private MongoRepository(){}

    public static MongoRepository getInstance(){
        return (instance == null) ? (new MongoRepository()) : instance;
    }

    public void insert(T data){
        repository.insertOne(IDocument.toDocument(data));
    }

    public void delete(T data){
        repository.deleteOne(IDocument.toDocument(data));
    }

    public Iterable<Document> find(T data){
        return repository.find(IDocument.toDocument(data));
    }

    public Iterable<Document> findAll(){
        return repository.find();
    }

    public void deleteAll(){
        repository.deleteMany(new Document());
    }

    public Iterable<Document> findByName(By method, String data){
        BasicDBObject query = new BasicDBObject();
        String field = "";

        switch(method){
            case NAME: {
                Pattern pattern = Pattern.compile(data, Pattern.CASE_INSENSITIVE);
                Bson filter = Filters.regex("name", pattern);
                return repository.find(filter);
            }
            case CATEGORY: {
                field = "category";
            }
            case PRICE : {
                field = "price";
            }
            case MARKET:{
                field = "market";

            }
        }

        query.put(field, data);

        return repository.find(query);
    }

}
