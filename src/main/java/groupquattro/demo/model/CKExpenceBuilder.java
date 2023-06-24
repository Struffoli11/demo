package groupquattro.demo.model;

import groupquattro.demo.exceptions.SumIsNotCorrectException;
import groupquattro.demo.utils.Round;

import java.util.*;

public class CKExpenceBuilder implements ExpenceBuilder{

    private CKExpence cKExpence;


    public CKExpenceBuilder(){
        reset();
    }
    public CKExpenceBuilder reset(){
        cKExpence = new CKExpence();
        return this;
    }
    public CKExpenceBuilder reset(CKExpence e){
        if(e!=null){
            this.cKExpence = e;
        }
        else {
            this.reset();
        }
        return this;

    }

    public CKExpence build() {
        return cKExpence;
    }

    public CKExpenceBuilder cost(double cost) {
        cKExpence.setCost(cost);
        return this;
    }


    public CKExpenceBuilder date(Date date) {
        cKExpence.setDate(date);
        return this;
    }


    public CKExpenceBuilder description(String descr) {
        cKExpence.setDescription(descr);
        return this;
    }

    /**
     * By far the most important method. It creates a chest from a mapm of paying members
     * and an expense cost. If the total amount paid by the members does not reach the total
     * expense cost, the chest is created anyway. Therefore, to make the cost match
     * the total amount of money members paid, it should be a concern of the user.
     * If no SumIsNotCorrectException is thrown, an instance of {{@code groupquattro.demo.model.Chest}} is created, which in turn
     * holds a chestKey object.
     * @param cost of the expence
     * @param payingMembers for a certain expence
     * @return an instance of this class
     * @throws SumIsNotCorrectException when cost is different from the total amount members did pay.
     */
    public CKExpenceBuilder chest(double cost, Map<String, Double> payingMembers) throws SumIsNotCorrectException {
//        TreeMap<String, Double> orderedMembers = new TreeMap<String, Double>(payingMembers);
        String keyOwner = null;
        Key key;
        Map<String, Double> creditors = new LinkedHashMap<>();
        Chest chest;
        double qpc = Round.round(cost/payingMembers.keySet().size(), 2);
        double amountThatOpensTheChest = 0.00;
        double totalSpent = 0.00;
        cKExpence.setDebts(new ArrayList<Debt>());
        List<Debt> debts = cKExpence.getDebts();
        for(String member : payingMembers.keySet()){
            totalSpent = Round.round(totalSpent+payingMembers.get(member), 2);
            double diff = Round.round(payingMembers.get(member)-qpc, 2);
            if(diff< 0){
                //this member is a debtor
                //the debt is always negative!
                debts.add(new Debt(diff, member, cKExpence.getDescription()));
                amountThatOpensTheChest += -(diff);
            }
            else if (diff>0){
                //this member has rights to withdraw from chest
                //hence it's one of the owner of the key associated with the chest
                creditors.put(member, diff);
            }
        }
        //now two maps have been initialized
        //one is stored inside into the chest class
        //and the other has to be passed as
        // a parameter to the Key class constructor
        key = new Key(creditors);
        if(debts.isEmpty() || creditors.isEmpty()){
            chest = null;
        }else {
            chest = new Chest(Round.round(amountThatOpensTheChest, 2), key);
        }
        cKExpence.setChest(chest);
        if(totalSpent == cKExpence.getCost()) {
            return this;
        } else {
            throw new SumIsNotCorrectException(String.format("total is %.2f but cost is %.2f", totalSpent, cKExpence.getCost()));
        }

    }


    public CKExpenceBuilder payingMembers(Map<String, Double> payingMembers) {
        cKExpence.setPayingMembers(payingMembers);
        return this;
    }


    public CKExpenceBuilder key(Key key){
        cKExpence.getChest().setChestKey(key);
        return this;
    }

}
