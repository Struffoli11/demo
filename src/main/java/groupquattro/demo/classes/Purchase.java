package groupquattro.demo.classes;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "purchases")
public class Purchase {

    @Id
    private ObjectId id;

    private double cost;

    private String idPurchase;

    private String description;

    @DocumentReference
    private Group groupId;
}
