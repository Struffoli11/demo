package groupquattro.demo.model;

import groupquattro.demo.utils.Round;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "chests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chest {

    @Id
    private String id;
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

    public Chest(String id) {
        this.id = id;
    }

    public double withdraw(double amount) {
        this.setCurrentValue(Round.round(this.getCurrentValue()-amount, 2));
        return this.getCurrentValue();
    }

    public double deposit(double amount) {
        this.setCurrentValue(Round.round(this.getCurrentValue()-amount, 2));
        return this.getCurrentValue();
    }

    /**
     *
     * @return the percentage of completion of this chest. A chest will be opened, hence
     * complete, when the current value inside the chest will be equal to max amount
     * that can be stored in the chest itself.
     */
    public double computePercentage(){
        return Round.round(currentValue * 100 / max_amount, 2);
    }
}
