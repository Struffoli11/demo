package groupquattro.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "debts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Debt {

    @Id
    private ObjectId id;

    private String creditor;

    private String debtor;

    private double debt;

    public Debt(double debt, String creditor, String debtor) {
        this.debt = debt;
        this.debtor = debtor;
        this.creditor = creditor;
    }
}
