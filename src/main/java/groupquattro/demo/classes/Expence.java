package groupquattro.demo.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "expences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expence {

    @Id
    private ObjectId id;

    private double cost;

    private String idExpence;

    private String description;

    public Expence(double cost, String idExpence, String description) {
        this.cost = cost;
        this.idExpence = idExpence;
        this.description = description;
    }
}
