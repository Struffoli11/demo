package groupquattro.demo.model;

import groupquattro.demo.exceptions.UserNotDebtorException;
import groupquattro.demo.utils.DateFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A chest and key expence. It is meant to facilitate
 * the mem
 */
@Document(collection = "ckexpences")
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class CKExpence implements Expence{

    @Id
    private String id;
    @DocumentReference
    private Chest chest;
    @DocumentReference
    private List<Debt> debts;
    private String description;
    private double cost;
    private Date date;
    private Map<String, Double> payingMembers;
    public static groupquattro.demo.model.CKExpenceBuilder builder = new CKExpenceBuilder();

    public CKExpence(double cost, Map<String, Double> payingMembers,
                     String description, Date date, Chest chest,List<Debt> debts) {
        this.debts =  debts;
        this.date = date;
        this.cost = cost;
        this.payingMembers = payingMembers;
        this.description = description;
        this.chest = chest;
    }


    /**
     *
     * @param amount to withdraw from chest
     * @return the amount still within chest
     */
    public double withdraw(double amount){
        return this.chest.withdraw(amount);
    }

    public double deposit(double amount){
        return this.chest.deposit(amount);
    }

    private double getChestCompletion(){
        return this.chest.computePercentage();
    }

//    public int getNumberOfDebts(){
//        return this.debts.size();
//    }

    public Debt getUserDebt(String username) throws UserNotDebtorException {
        for(Debt d : debts){
            if(d.getDebtor().equals(username)){
                return d;
            }
        }
        throw new UserNotDebtorException("user "+ username+ " is not a debtor");
    }

    public Date getDate() {
        return DateFormatter.formatDatePersonalized(date);
    }

}
