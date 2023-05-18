package groupquattro.demo.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "debts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Debt {
    @Id
    private ObjectId id;

    private String idDebt;

    @DocumentReference
    private User creditor;

    @DocumentReference
    private User debtor;

    private double debt;

    @DocumentReference
    private Expence idExpence;

    public Debt(String idDebt , double debt) {
        this.idDebt = idDebt;
        this.debt = debt;
    }
}
