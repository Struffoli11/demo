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
public class ChestExpence extends Expence{

    @Id
    protected ObjectId id;

    protected  double cost;

    protected Date date;

    protected String description;

    protected  Map<String, Double> payingMembers;

    private ExpenceChest ec;

    public static class ChestExpenceBuilder{

    }

}
