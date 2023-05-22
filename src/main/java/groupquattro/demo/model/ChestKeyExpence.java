package groupquattro.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document(collection = "expences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChestKeyExpence extends Expence{

    @Id
    private ObjectId id;

    private  double cost;

    private Date date;

    private String description;

    private Map<String, Double> payingMembers;

    private ExpenceChest ec;

    private ExpenceKey ek;

    public static class ChestKeyExpenceBuilder{

    }
}
