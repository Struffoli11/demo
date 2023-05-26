package groupquattro.demo.api;

import groupquattro.demo.exceptions.ChestNotOpenedException;
import groupquattro.demo.exceptions.WrongExpenceTypeException;
import groupquattro.demo.model.CKExpence;
import groupquattro.demo.model.CKExpenceBuilder;
import groupquattro.demo.model.Chest;
import groupquattro.demo.model.Group;
import groupquattro.demo.services.CKExpenceService;
import groupquattro.demo.services.GroupService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/CKexpences")
public class CKExpenceAPI {
    @Autowired
    private GroupService gs;

    @Autowired
    private CKExpenceService cks;

    @GetMapping
    public ResponseEntity<List<CKExpence>> getAllExpences(){
        return new ResponseEntity<List<CKExpence>>(cks.getAllCKExpences(), HttpStatus.OK);
    }

    @GetMapping("/{idExpence}")
    public ResponseEntity<Optional<CKExpence>> getExpenceById(@PathVariable String idExpence){
        return new ResponseEntity<Optional<CKExpence>>(cks.findCKExpenceByIdExpence(idExpence), HttpStatus.OK);
    }

    @GetMapping("/{groupName}/group-expences")
    public List<CKExpence> getExpencesByDescription(@PathVariable String groupName, @RequestBody Map<String, String> query) {
        Optional<Group> g = gs.findGroupByGroupName(groupName);
        List<CKExpence> expencesMatchingGroup = cks.findByGroupName(groupName);
        for(CKExpence re : expencesMatchingGroup){
            System.out.println(re);
        }
        System.out.println(query.get("description"));
        List<CKExpence> expencesMatchingGroupAndDescritpion = new ArrayList<>();
        for(CKExpence re : expencesMatchingGroup){
            if(re.getDescription().equals(query.get("description")))
                expencesMatchingGroupAndDescritpion.add(re);
        }
        return expencesMatchingGroupAndDescritpion;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CKExpence createExpence(@RequestBody Document expence) throws ParseException, WrongExpenceTypeException {
        Map<String, Double> payingMembers = expence.get("payingMembers", LinkedHashMap.class);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        CKExpenceBuilder ckeb = new CKExpenceBuilder();
        CKExpence cke = ckeb
                .date(sdf.parse(expence.getString("date")))
                .description(expence.getString("description"))
                .cost(expence.getDouble("cost"))
                .payingMembers(payingMembers)
                .groupName(expence.getString("groupName")).build();

        return cks.createCKExpence(ckeb, cke);
    }

    @PostMapping("/{expenceId}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public Chest withdrawFromChest(@PathVariable String expenceId, @RequestBody String user) throws ChestNotOpenedException {
        CKExpence cke = null;
        Optional<CKExpence> result;
        Chest chest = null;
        double newvalue, oldvalue;
        newvalue=oldvalue=0;
        boolean update = false;
        if((result= cks.findCKExpenceByIdExpence(expenceId)).isPresent()){
            cke = result.get();
            chest = cke.getChest();
            if(chest.isOpen()) {
                Map<String, Double> chestKeyOwners = chest.getChestKey().getListOfOwners();
                for (String owner : chestKeyOwners.keySet()) {
                    if (user.equals(owner)) {
                        update = true;
                        double withdrawable = chestKeyOwners.get(user);
                        oldvalue = chest.getCurrentValue();
                        newvalue = chest.getCurrentValue() - withdrawable;

                    }
                }
                if(newvalue>0 && update) {
                    chest.setCurrentValue(newvalue);
                    chestKeyOwners.remove(user);

                    //update collection
                    chest = cks.updateChest(oldvalue, chest, cke);
                }
                else if (update){
                    //delete chest
                    chest = cks.deleteChest(chest, cke);
                }
            }
            else {
                throw new ChestNotOpenedException();
            }
        }
        return chest;
    }

    @PostMapping("/{expenceId}/payment")
    @ResponseStatus(HttpStatus.OK)
    public Chest payInChest(@PathVariable String expenceId, @RequestBody String username){
        CKExpence cke = null;
        Optional<CKExpence> result;
        Chest oldChest;
        Chest updatedChest = null;
        if((result= cks.findCKExpenceByIdExpence(expenceId)).isPresent()) {
            cke = result.get();
            double amount = cke.getCost()/cke.getPayingMembers().keySet().size();
            oldChest = cke.getChest();
            double currentValue = oldChest.getCurrentValue();
            oldChest.setCurrentValue(currentValue + amount);
            if(oldChest.getCurrentValue() >= oldChest.getMax_amount()){
                oldChest.setOpen(true);
            }

            //update operations
            updatedChest = cks.updateChest(currentValue, oldChest, cke);
        }
        return updatedChest;
    }

}
