package groupquattro.demo.model;

import groupquattro.demo.exceptions.WrongExpenceTypeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


@Document(collection = "expences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RomanaExpence extends Expence{

    @Id
    protected ObjectId id;

    protected  double cost;

    protected Date date;

    protected String description;

    protected  Map<String, Double> payingMembers;

    @DocumentReference
    private List<Debt> debts;

    public RomanaExpence(double cost, Map<String, Double> payingMembers,
                         List<Debt> debts, String description, Date date) {
        this.cost = cost;
        this.payingMembers = payingMembers;
        this.description = description;
        this.date = date;
        this.debts = debts;
    }

    @Document(collection = "debts")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Debt {
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
    public static class RomanaExpenceBuilder{
        public static List<Debt> computeDebts(double cost, Map<String, Double> payingMembers){
            return null;
        }

        public RomanaExpenceBuilder computeDebts() throws WrongExpenceTypeException {
            boolean romana = false;
            if(this.cost==0 || this.payingMembers == null)
                throw new IllegalStateException("cost equal to zero or/and paying members not initialized.");
            int nMembers = payingMembers.size();
            double costPerMember = round(cost/nMembers, 2);

            //check for possibility that a member put money for everyone
            for(String payingMember : payingMembers.keySet()) {
                Double share = payingMembers.get(payingMember);
                if (share == cost) {
                    //for every other member set a new debt
                    //because they owe money to this member
                    this.debts = new ArrayList<Debt>();
                    for (String nonPayingMember : payingMembers.keySet()) {
                        if (nonPayingMember.equals(payingMember)) continue;
                        this.debts.add(new Debt(costPerMember, payingMember, nonPayingMember));
                    }
                    return this;
                }
            }

            for(String payingMember : payingMembers.keySet()) {
                Double share = payingMembers.get(payingMember);
                //ìif there is a member that did not pay costPerMember then
                //throw exception because user invoked the wrong type of ExpenceBuilder
                if(share!=costPerMember) throw new WrongExpenceTypeException();
            }

            debts = null;
            return this;
        }

        public static double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();

            BigDecimal bd = BigDecimal.valueOf(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }
}
