package groupquattro.demo.model;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class CKExpenceBuilder implements ExpenceBuilder{

    private CKExpence cKExpence;

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


    public CKExpenceBuilder payingMembers(Map<String, Double> pMembers) {
        cKExpence.setPayingMembers(pMembers);
        return this;
    }


    public CKExpenceBuilder groupName(String groupName) {
        cKExpence.setGroupName(groupName);
        return this;
    }
    public CKExpenceBuilder chest(double cost, Map<String, Double> payingMembers){
        TreeMap<String, Double> orderedMembers = new TreeMap<String, Double>(payingMembers);
        String keyOwner = null;
        Key key = new Key(payingMembers);
        Chest chest = new Chest(cost, key);
        cKExpence.setChest(chest);
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
