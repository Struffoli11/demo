package groupquattro.demo.services;

import groupquattro.demo.exceptions.ChestNotOpenedException;
import groupquattro.demo.exceptions.ChestOpenedException;
import groupquattro.demo.exceptions.UserNotDebtorException;
import groupquattro.demo.exceptions.UserNotOwnerException;
import groupquattro.demo.model.*;
import groupquattro.demo.repos.ChestRepository;
import groupquattro.demo.repos.KeyRepository;
import groupquattro.demo.repos.MExpenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class MExpenceService extends CKExpenceService{

    @Autowired
    MongoTemplate mt;

    @Autowired
    private MExpenceRepository mr;

    @Autowired
    private KeyRepository kr;

    @Autowired
    private ChestRepository cr;

    public MExpence createExpence(CKExpenceBuilder ckeb, MExpence cke, String groupOwner)
    {
        cke = createChestAndKey(ckeb, cke, groupOwner);
        mt.update(Group.class)
                .matching(Criteria.where("groupName").is(cke.getGroupName()))
                .apply(new Update().push("expences").value(cke)).first();

        return cke;
    }
    public MExpence createChestAndKey(CKExpenceBuilder ckeb, MExpence ckExpence, String groupOwner) {

        MExpence ckExpence1 = ckeb.chest(ckExpence.getCost(), ckExpence.getPayingMembers(), groupOwner).buildM();
        Chest chest = ckExpence1.getChest();
        Key chestKey = chest.getChestKey();


        kr.insert(chestKey);
        chest.setChestKey(null);

        cr.insert(chest);

        mt.update(Chest.class)
                .matching(Criteria.where("_id").is(chest.getId()))
                .apply(new Update().set("chestKey", chestKey)).first();

        mr.insert(ckExpence1);

        mt.update(CKExpence.class)
                .matching(Criteria.where("_id").is(ckExpence1.getId()))
                .apply(new Update().set("chest", chest)).first();

        chest.setChestKey(chestKey);
        return ckExpence1;
    }

    public boolean deposit(MExpence me, Chest chest, String user) throws ChestOpenedException, UserNotDebtorException{
        if(!chest.isOpen()) {
            Map<String, Double> expenceDebtors = me.getDebtors();
            if (expenceDebtors.get(user)!=null) {
                double amount = expenceDebtors.get(user);
                double newValue = chest.deposit(amount);
                expenceDebtors.remove(user);
                if(expenceDebtors.isEmpty()) {
                    //everyone got their share, delete chest
                    chest.setOpen(true);
                    //update collection chests
                    //with new value stored in chest
                    chest = updateChest(chest, me);
                    return true;
                }
                else {
                    //update collection chests
                    //with new value stored in chest
                    chest = updateChest(chest, me);
                    return false;
                }
            }
            else {
                throw new UserNotDebtorException("not a debtor!");
            }
        }
        else {
            throw new ChestOpenedException();
        }
    }

    public boolean withdraw(MExpence me, Chest chest, String user) throws UserNotOwnerException, ChestNotOpenedException{
        if(chest.isOpen()) {
            Map<String, Double> chestKeyOwners = chest.getChestKey().getListOfOwners();
            if (chestKeyOwners.get(user)!=null) {
                double amount = chestKeyOwners.get(user);
                double newValue = chest.withdraw(amount);
                chestKeyOwners.remove(user);
                if(chestKeyOwners.isEmpty()) {
                    //everyone got their share, delete chest
                    chest = deleteChest(chest, me);
                    return true;
                }
                else {
                    //update collection chests
                    //with new value stored in chest
                    chest = updateChest(chest, me);
                    return false;
                }
            }
            else {
                throw new UserNotOwnerException("not an owner!");
            }
        }
        else {
            throw new ChestNotOpenedException();
        }
    }

    public Chest updateChest(Chest chest, MExpence cke) {
        mt.save(chest.getChestKey());
        mt.save(chest);
        mt.save(cke);
        return chest;
    }

    public Chest deleteChest(Chest chest, MExpence cke) {

        Chest removedChest = mt.findAndRemove(Query.query(Criteria.where("_id").is(chest.getId())),
                Chest.class, "chests");

        mt.update(MExpence.class)
                .matching(Criteria.where("_id").is(cke.getId()))
                .apply(new Update().unset("chest")).first();

        //remove the key associated with the chest
        Key keyToBeRemoved = mt.findAndRemove(Query.query(Criteria.where("_id").is(removedChest.getChestKey().getId())),
                Key.class, "keys");

        System.out.println(removedChest);
        return removedChest;
    }
    public Optional<MExpence> findMExpenceById(String id){
        return mr.findExpenceById(id);
    }

}
