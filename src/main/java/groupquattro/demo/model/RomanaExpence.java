package groupquattro.demo.model;

import groupquattro.demo.exceptions.WrongExpenceTypeException;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


@EqualsAndHashCode(callSuper = false)
@Document(collection = "expences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RomanaExpence implements Expence{

    @Id
    private ObjectId id;

    private String description;

    private double cost;

    private List<Debt> debts;

    private Date date;

    private String groupName;
    private Map<String, Double> payingMembers;
    public RomanaExpence(double cost, Map<String, Double> payingMembers,
                         List<Debt> debts, String description, Date date) {
        this.cost = cost;
        this.payingMembers = payingMembers;
        this.description = description;
        this.date = date;
        this.debts = debts;
    }
}
