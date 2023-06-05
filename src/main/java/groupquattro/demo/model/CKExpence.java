package groupquattro.demo.model;

import groupquattro.demo.utils.Round;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

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
    private String id;

    @DocumentReference
    private Chest chest;

    @Field(name = "debtors")
    private Map<String, Double> debtors;

    private String groupName;

    private String description;

    private double cost;

    private Date date;

    private Map<String, Double> payingMembers;
    public CKExpence(double cost, Map<String, Double> payingMembers,
                     String description, Date date, String groupName, Chest chest, Map<String, Double> debtors) {
        this.date = date;
        this.cost = cost;
        this.groupName = groupName;
        this.description = description;
        this.chest = chest;
        this.debtors = debtors;
    }

    public CKExpence(double cost, Map<String, Double> payingMembers,
                     Date date, String description, String groupName){
        this.date = date;
        this.cost = cost;
        this.groupName = groupName;
        this.description = description;
    }

    /**
     *
     * @param amount to withdraw from chest
     * @return the amount still within chest
     */
    public double withdraw(double amount){
        return this.chest.withdraw(amount);
    }

    public double deposit(double amount){
        return this.chest.deposit(amount);
    }
}
