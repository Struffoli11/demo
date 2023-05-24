package groupquattro.demo.model;

import groupquattro.demo.exceptions.WrongExpenceTypeException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RomanaExpenceBuilder implements ExpenceBuilder{

    private RomanaExpence romanaExpence;

    public RomanaExpenceBuilder(){
        reset();
    }

    public RomanaExpenceBuilder reset(){
        this.romanaExpence = new RomanaExpence();
        return this;
    }

    public RomanaExpence build() {
        return romanaExpence;
    }


    public RomanaExpenceBuilder cost(double cost) {
        romanaExpence.setCost(cost);
        return this;
    }


    public RomanaExpenceBuilder date(Date date) {
        romanaExpence.setDate(date);
        return this;
    }

    public RomanaExpenceBuilder description(String descr) {
        romanaExpence.setDescription(descr);
        return this;
    }

    public RomanaExpenceBuilder payingMembers(Map<String, Double> pMembers) {
        romanaExpence.setPayingMembers(pMembers);
        return this;
    }

    public RomanaExpenceBuilder groupName(String groupName) {
        romanaExpence.setGroupName(groupName);
        return this;
    }


    public RomanaExpenceBuilder debts(List<Debt> debts) {
        romanaExpence.setDebts(debts);
        return this;
    }

    public RomanaExpenceBuilder computeDebts() throws WrongExpenceTypeException {
        System.out.println("Compute Debts");
        boolean romana = false;
        if(romanaExpence.getCost()==0 || romanaExpence.getPayingMembers() == null)
            throw new IllegalStateException("cost equal to zero or/and paying members not initialized.");
        double cost = romanaExpence.getCost();
        Map<String, Double> payingMembers = romanaExpence.getPayingMembers();
        List<Debt> debts = new ArrayList<>();
        int nMembers = payingMembers.size();
        double costPerMember = round(cost/nMembers, 2);

        //check for possibility that a member put money for everyone
        for(String payingMember : payingMembers.keySet()) {
            Double share = payingMembers.get(payingMember);
            if (share == cost) {
                //for every other member set a new debt
                //because they owe money to this member
                for (String nonPayingMember : payingMembers.keySet()) {
                    if (nonPayingMember.equals(payingMember)) continue;
                    debts.add(new Debt(costPerMember, payingMember, nonPayingMember));
                }
                romanaExpence.setDebts(debts);
                return this;
            }
        }

        for(String payingMember : payingMembers.keySet()) {
            Double share = payingMembers.get(payingMember);
            //ìf there is a member that did not pay costPerMember then
            //throw exception because user invoked the wrong type of ExpenceBuilder
            if(share!=costPerMember) throw new WrongExpenceTypeException();
        }

        return this;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
