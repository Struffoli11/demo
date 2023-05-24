package groupquattro.demo.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Document(collection = "ckexpences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CKExpence implements Expence{

    @Id
    private ObjectId id;

    @DocumentReference
    private Chest chest;

    private String groupName;

    private String description;

    private double cost;

    private Date date;

    private Map<String, Double> payingMembers;
    public CKExpence(double cost, Map<String, Double> payingMembers,
                     String description, Date date, String groupName, Chest chest) {
        this.date = date;
        this.cost = cost;
        this.groupName = groupName;
        this.description = description;
        this.chest = chest;
    }

    public CKExpence(double cost, Map<String, Double> payingMembers,
                     Date date, String description, String groupName){
        this.date = date;
        this.cost = cost;
        this.groupName = groupName;
        this.description = description;
    }
}
