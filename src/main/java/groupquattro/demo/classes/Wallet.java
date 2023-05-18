package groupquattro.demo.classes;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "wallets")
public class Wallet {

    @Id
    private ObjectId id;

    private double coins;

    private String idWallet;
}
