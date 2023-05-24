package groupquattro.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.nio.DoubleBuffer;

@Document(collection = "chests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chest {

    @Id
    private ObjectId id;

    private double max_amount;

    private boolean open;

    private double currentValue;

    @DocumentReference
    @Field(name = "chestKey")
    private Key chestKey;

    public Chest(double max_amount, Key chestKey){
        this.currentValue = 0;
        this.max_amount = max_amount;
        this.chestKey = chestKey;
        open = false;
    }
}
