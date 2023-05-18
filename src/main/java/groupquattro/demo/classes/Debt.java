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
    private User idCreditor;

    @DocumentReference
    private User idDebtor;

    private double debt;
}
