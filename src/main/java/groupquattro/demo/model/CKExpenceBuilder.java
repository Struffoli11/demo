package groupquattro.demo.model;

import groupquattro.demo.utils.Round;

import java.util.*;

public class CKExpenceBuilder implements ExpenceBuilder{

    private CKExpence cKExpence;

    private MExpence mExpence;


    public CKExpenceBuilder(){
        reset();
    }
    public CKExpenceBuilder reset(){
        cKExpence = new CKExpence();
        return this;
    }
    public CKExpence build() {
        return cKExpence;
    }

    public MExpence buildM(){
        this.mExpence = new MExpence(cKExpence);
        return mExpence;
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
//
//    public CKExpenceBuilder chest(double cost, Map<String, Double> payingMembers){
//        TreeMap<String, Double> orderedMembers = new TreeMap<String, Double>(payingMembers);
//        String keyOwner = null;
//        Key key = new Key(payingMembers);
//        Chest chest = new Chest(cost, key);
//        cKExpence.setChest(chest);
//        return this;
//    }


    public CKExpenceBuilder chest(double cost, Map<String, Double> payingMembers){
//        TreeMap<String, Double> orderedMembers = new TreeMap<String, Double>(payingMembers);
        String keyOwner = null;
        Key key;
        Map<String, Double> creditors = new LinkedHashMap<>();
        Chest chest;
        double qpc = Round.round(cost/payingMembers.keySet().size(), 2);
        double amountThatOpensTheChest = 0.00;
        cKExpence.setDebtors(new LinkedHashMap<>());
        for(String member : payingMembers.keySet()){
            double diff = Round.round(payingMembers.get(member)-qpc, 2);
            if(diff<= 0){
                //this member is a debtor
                cKExpence.getDebtors().put(member, diff);
                amountThatOpensTheChest += -(diff);
            }
            else{
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
        chest = new Chest(Round.round(amountThatOpensTheChest, 2), key);
        cKExpence.setChest(chest);
        return this;
    }

    public CKExpenceBuilder chest(double cost, Map<String, Double> payingMembers, String groupOwner){
//        TreeMap<String, Double> orderedMembers = new TreeMap<String, Double>(payingMembers);
        String keyOwner = null;
        Key key;
        Map<String, Double> creditors = new LinkedHashMap<>();
        Chest chest;
        if(groupOwner.equals("")) {
            //ckexpenceAPI
            double qpc = Round.round(cost / payingMembers.keySet().size(), 2);
            double amountThatOpensTheChest = 0.00;
            cKExpence.setDebtors(new LinkedHashMap<>());
            for (String member : payingMembers.keySet()) {
                double diff = Round.round(payingMembers.get(member) - qpc, 2);
                if (diff <= 0) {
                    //this member is a debtor
                    cKExpence.getDebtors().put(member, diff);
                    amountThatOpensTheChest += -(diff);
                } else {
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
            chest = new Chest(Round.round(amountThatOpensTheChest, 2), key);
            cKExpence.setChest(chest);
        }
        else{
            //milexpenceAPI
            creditors.put(groupOwner, cost);
            key = new Key(creditors);
            chest = new Chest(cost, key);
            cKExpence.setChest(chest);
        }
        return this;
    }


    public CKExpenceBuilder payingMembers(Map<String, Double> pMembers) {
        cKExpence.setPayingMembers(pMembers);
        return this;
    }

    /**
     * Used into MilaneseExpenceAPI
     * @param payingMembers
     * @param groupOwner
     * @return
     */
    public CKExpenceBuilder payingMembers(Map<String, Double> payingMembers, String groupOwner) {
        cKExpence.setPayingMembers(payingMembers);
        for(String pMember : payingMembers.keySet()){
            double amount = -payingMembers.get(pMember);
            payingMembers.put(pMember, amount);
        }
        //debtors not null when chest() is invoked
        cKExpence.setDebtors(payingMembers);
        return this;
    }

    public CKExpenceBuilder groupName(String groupName) {
        cKExpence.setGroupName(groupName);
        return this;
    }

    public CKExpenceBuilder key(Key key){
        cKExpence.getChest().setChestKey(key);
        return this;
    }

//    public ExpenceBuilder buildChest(){
//        Chest chest = new Chest(this.cost);
//
//        return this;
//    }




}
