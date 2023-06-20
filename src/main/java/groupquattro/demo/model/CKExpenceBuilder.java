package groupquattro.demo.model;

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

    public CKExpenceBuilder chest(double cost, Map<String, Double> payingMembers){
//        TreeMap<String, Double> orderedMembers = new TreeMap<String, Double>(payingMembers);
        String keyOwner = null;
        Key key;
        Map<String, Double> creditors = new LinkedHashMap<>();
        Chest chest;
        double qpc = Round.round(cost/payingMembers.keySet().size(), 2);
        double amountThatOpensTheChest = 0.00;
        cKExpence.setDebts(new ArrayList<Debt>());
        List<Debt> debts = cKExpence.getDebts();
        for(String member : payingMembers.keySet()){
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
        return this;
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
