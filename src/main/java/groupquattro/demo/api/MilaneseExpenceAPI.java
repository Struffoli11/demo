package groupquattro.demo.api;

import groupquattro.demo.exceptions.ChestNotOpenedException;
import groupquattro.demo.exceptions.UserNotFoundException;
import groupquattro.demo.exceptions.UserNotOwnerException;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/milexpences")
public class MilaneseExpenceAPI {
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
        Map<String, Double> payingMembers = new LinkedHashMap<String, Double>();
        Map<String, String> dataMap= expence.get("payingMembers", LinkedHashMap.class);

//        System.out.println("Is data empty? "+ (dataMap.isEmpty() ? "jah!" : "nein!"));

        for(String member : dataMap.keySet()){
            payingMembers.put(member, Double.valueOf(dataMap.get(member)));
        }
        double cost = Double.valueOf(expence.getString("cost")).doubleValue();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        CKExpenceBuilder ckeb = new CKExpenceBuilder();
        Group g= gs.findGroupByGroupName(expence.getString("groupName")).get();
        String groupOwner = g.getGroupOwner().getUsername();
        CKExpence cke = ckeb
                .date(sdf.parse(expence.getString("date")))
                .description(expence.getString("description"))
                .cost(cost)
                .payingMembers(payingMembers, groupOwner)
                .groupName(expence.getString("groupName")).build();

        return cks.createCKExpence(ckeb, cke, groupOwner);
    }

    @GetMapping("/{expenceId}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public Double withdrawFromChest(@PathVariable String expenceId, @RequestBody String user) throws ChestNotOpenedException, UserNotOwnerException {
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
                        newvalue = round(chest.getCurrentValue() - withdrawable, 2);

                    }
                }
                if(newvalue>0 && update) {
                    chest.setCurrentValue(newvalue);
                    chestKeyOwners.remove(user);

                    //update collection
                    chest = cks.updateChest(oldvalue, chest, cke);
                    if(chest.getChestKey().getListOfOwners().isEmpty()){
                        cks.deleteChest(chest, cke);
                    }
                }
                else if (update){
                    //delete chest
                    chest = cks.deleteChest(chest, cke);

                }
                else {
                    throw new UserNotOwnerException("not an owner!");
                }
            }
            else {
                throw new ChestNotOpenedException();
            }
        }
        return chest.getCurrentValue();
    }

    @PostMapping("/{expenceId}/payment")
    @ResponseStatus(HttpStatus.OK)
    public Chest depositChest(@PathVariable String expenceId, @RequestBody String username) {
        CKExpence cke = null;
        Optional<CKExpence> result;
        Chest oldChest = null;
        Chest updatedChest = null;
        boolean found = false;
        double currentValue = 0;
        if ((result = cks.findCKExpenceByIdExpence(expenceId)).isPresent()) {
            cke = result.get();
            //check that this member is a debtor
            for (String debtor : cke.getDebtors().keySet())
                if (username.equals(debtor)) {
                    found = true;
                    double value = cke.getDebtors().get(debtor);
                    oldChest = cke.getChest();
                    currentValue = oldChest.getCurrentValue();
                    oldChest.setCurrentValue(round(currentValue - value, 2));
                    if (oldChest.getCurrentValue() >= oldChest.getMax_amount()) {
                        oldChest.setOpen(true);
                    }
                    break;
                }

        }
        if(found) {
            cke.getDebtors().remove(username);
            //update operations
            updatedChest = cks.updateChest(currentValue, oldChest, cke);
        }
        else throw new UserNotFoundException("not a debtor");
        return updatedChest;
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
