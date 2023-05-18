package groupquattro.demo.classes;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "groups")
public class Group {
    @Id
    private ObjectId id;

    @DocumentReference
    private List<User> members;

    private String groupName;

    private String idGroup;

    @DocumentReference
    private List<Purchase> purchases;



}
