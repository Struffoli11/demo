package groupquattro.demo.services;

import groupquattro.demo.model.*;
import groupquattro.demo.repos.CKExpenceRepository;
import groupquattro.demo.repos.ChestRepository;
import groupquattro.demo.repos.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

@Service
public class CKExpenceService {
    @Autowired
    MongoTemplate mt;

    @Autowired
    private KeyRepository kr;

    @Autowired
    private ChestRepository cr;

    @Autowired
    private CKExpenceRepository ckr;

    private CKExpenceBuilder ckeb;

    public Optional<CKExpence> findExpenceByIdExpence(String idExpence){
        return ckr.findCKExpenceById(idExpence);
    }
    public List<CKExpence> findByGroupName(String groupName) {
        return ckr.findByGroupName(groupName);
    }


    public List<CKExpence> getAllCKExpences(){
        return ckr.findAll();
    }

    public CKExpence createCKExpence(CKExpenceBuilder ckeb, CKExpence cke, String groupOwner)
    {
        cke = createChestAndKey(ckeb, cke, groupOwner);
        mt.update(Group.class)
                .matching(Criteria.where("groupName").is(cke.getGroupName()))
                .apply(new Update().push("expences").value(cke)).first();

        return cke;
    }

    public CKExpence createChestAndKey(CKExpenceBuilder ckeb, CKExpence ckExpence, String groupOwner) {

        CKExpence ckExpence1 = ckeb.chest(ckExpence.getCost(), ckExpence.getPayingMembers(), groupOwner).build();
        Chest chest = ckExpence1.getChest();
        Key chestKey = chest.getChestKey();


        kr.insert(chestKey);
        chest.setChestKey(null);

        cr.insert(chest);

        mt.update(Chest.class)
                .matching(Criteria.where("_id").is(chest.getId()))
                .apply(new Update().set("chestKey", chestKey)).first();

        ckr.insert(ckExpence1);

        mt.update(CKExpence.class)
                .matching(Criteria.where("_id").is(ckExpence1.getId()))
                .apply(new Update().set("chest", chest)).first();

        chest.setChestKey(chestKey);
        return ckExpence1;
    }

    public Optional<CKExpence> findCKExpenceByIdExpence(String idExpence) {
        return ckr.findCKExpenceById(idExpence);
    }

    public Chest updateChest(double currentValue, Chest chest, CKExpence cke) {
        CKExpence oldExpence = ckr.findCKExpenceById(cke.getId().toString()).get();
        Map<String, Double> oldOwners = oldExpence.getChest().getChestKey().getListOfOwners();
        if(chest.getChestKey().getListOfOwners().keySet().size()!=oldOwners.keySet().size()){
            //update key
            mt.update(Key.class)
                    .matching(Criteria.where("_id").is(chest.getChestKey().getId()))
                    .apply(new Update().set("listOfOwners", chest.getChestKey().getListOfOwners())).first();

            //update chest
            mt.update(Chest.class)
                    .matching(Criteria.where("_id").is(chest.getId()))
                    .apply(new Update().set("currentValue", chest.getCurrentValue())).first();

        }
        if(chest.getCurrentValue()!=currentValue){
            if(cke.getDebtors().keySet().size()!=oldExpence.getDebtors().keySet().size()){
                mt.update(CKExpence.class)
                        .matching(Criteria.where("_id").is(cke.getId()))
                        .apply(new Update().set("debtors", cke.getDebtors())).first();
            }
            if(chest.getCurrentValue() == chest.getMax_amount()){
                mt.update(Chest.class)
                        .matching(Criteria.where("_id").is(chest.getId()))
                        .apply(new Update().set("open", true)).first();
            }
            mt.update(Chest.class)
                    .matching(Criteria.where("_id").is(chest.getId()))
                    .apply(new Update().set("currentValue", chest.getCurrentValue())).first();
        }

        return chest;
    }

    public Chest deleteChest(Chest chest, CKExpence cke) {

        Chest removedChest = mt.findAndRemove(Query.query(Criteria.where("_id").is(chest.getId())),
                Chest.class, "chests");

        mt.update(CKExpence.class)
                        .matching(Criteria.where("_id").is(cke.getId()))
                .apply(new Update().unset("chest")).first();

        //remove the key associated with the chest
        Key keyToBeRemoved = mt.findAndRemove(Query.query(Criteria.where("_id").is(removedChest.getChestKey().getId())),
                Key.class, "keys");

        System.out.println(removedChest);
            return removedChest;
    }
}
