package groupquattro.demo.api;

import groupquattro.demo.exceptions.*;
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
        //------data conversion start
        Map<String, Double> payingMembers = new LinkedHashMap<String, Double>();
        Map<String, String> dataMap= expence.get("payingMembers", LinkedHashMap.class);

//        System.out.println("Is data empty? "+ (dataMap.isEmpty() ? "jah!" : "nein!"));

        for(String member : dataMap.keySet()){
            payingMembers.put(member, Double.valueOf(dataMap.get(member)));
        }
        double cost = Double.valueOf(expence.getString("cost")).doubleValue();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        //----data conversion end

        //should substitute with DTO
        CKExpenceBuilder ckeb = new CKExpenceBuilder();
        CKExpence cke = ckeb
                .date(sdf.parse(expence.getString("date")))
                .description(expence.getString("description"))
                .cost(cost)
                .payingMembers(payingMembers)
                .groupName(expence.getString("groupName")).build();

        return cks.createExpence(ckeb, cke, "");
    }

    //DA PROTEGGERE CON RUOLO ADMIN
    @GetMapping("/{expenceId}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public void withdrawFromChest(@PathVariable String expenceId, @RequestBody String user) throws ChestNotOpenedException, UserNotOwnerException, WrongExpenceTypeException {
        CKExpence cke;
        Optional<CKExpence> result;
        Chest chest;
        if((result= cks.findCKExpenceByIdExpence(expenceId)).isPresent()) {
            cke = result.get();
            chest = cke.getChest();
            //check that this use is an owner
            try{
                cks.withdraw(cke, chest, user);
            }catch (UserNotOwnerException e){
                throw e;
            }catch (ChestNotOpenedException e){
                throw e;
            }
        }
        else{
            throw new WrongExpenceTypeException();
        }
    }

    @PostMapping("/{expenceId}/payment")
    @ResponseStatus(HttpStatus.OK)
    public void depositChest(@PathVariable String expenceId, @RequestBody String username) throws UserNotDebtorException, ChestOpenedException, WrongExpenceTypeException {
        CKExpence cke;
        Optional<CKExpence> result;
        Chest chest;
        if ((result = cks.findCKExpenceByIdExpence(expenceId)).isPresent()) {
            cke = result.get();
            chest = cke.getChest();
            //check that this member is a debtor
            try{
                cks.deposit(cke, chest, username);
            }catch (UserNotDebtorException e){
                throw e;
            }catch (ChestOpenedException e){
                throw e;
            }
        }
        else {
            throw new WrongExpenceTypeException();
        }
    }
}
