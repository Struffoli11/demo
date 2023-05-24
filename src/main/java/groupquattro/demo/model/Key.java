package groupquattro.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "keys")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Key {

    @Id
    private ObjectId id;

    private Map<String, Double> listOfOwners;
    public Key(Map<String, Double> listOfOwners) {
        this.listOfOwners = listOfOwners;
    }


}
