package groupquattro.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;

    private String username;

    //private String email;

    private String password;

    @Field(value = "groups", write = Field.Write.NON_NULL) @DocumentReference(lazy = true)
    private List<Group> groups;

    @Field(value = "wallet", write = Field.Write.NON_NULL)@DocumentReference(lazy = true)
    private Wallet wallet;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.groups = new ArrayList<Group>();
        this.wallet = null;

    }
}
