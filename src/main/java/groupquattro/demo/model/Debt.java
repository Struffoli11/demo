package groupquattro.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "debts")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Debt {
    @Id
    private String id;
    @NonNull
    private String debtor;
    private String expenceDescription;
    //this is a negative value

    private double debt;

    public Debt(double debt, String debtor, String expenceDescription) {
        this.debt = debt;
        this.debtor = debtor;
        this.expenceDescription = expenceDescription;
    }

//    public Debt(String member, double debt) {
//        this.debt = debt;
//        this.debtor = member;
//        this.id = "-1";
//    }
}
