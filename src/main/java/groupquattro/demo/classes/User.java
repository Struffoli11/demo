package groupquattro.demo.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;

    private String username;

    private String password;

    @DocumentReference
    private List<Group> groups;

    @DocumentReference
    private List<Debt> debts;

    @DocumentReference
    private List<Wallet> walletId;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
