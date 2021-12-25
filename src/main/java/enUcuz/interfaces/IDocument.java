package enUcuz.interfaces;
import enUcuz.model.AbstractDocument;
import org.bson.Document;

public interface IDocument {
    public static <T extends AbstractDocument> Document toDocument(AbstractDocument obj){
        Document document = new Document();
        obj.attributes.forEach((k,v) -> {document.append(k,v);});
        return document;
    }
}
