package groupquattro.demo.api;

import groupquattro.demo.exceptions.*;
import groupquattro.demo.model.*;
import groupquattro.demo.services.CKExpenceService;
import groupquattro.demo.services.GroupService;
import groupquattro.demo.services.MExpenceService;
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
    private MExpenceService ms;

    @Autowired
    private CKExpenceService cks;

    @GetMapping("/{idExpence}")
    public ResponseEntity<Optional<MExpence>> getExpenceById(@PathVariable String idExpence){
        return new ResponseEntity<Optional<MExpence>>(ms.findMExpenceById(idExpence), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CKExpence createExpence(@RequestBody Document expence) throws ParseException, WrongExpenceTypeException {
        //data conversion
        //------data conversion start------
        Map<String, Double> payingMembers = new LinkedHashMap<String, Double>();
        Map<String, String> dataMap= expence.get("payingMembers", LinkedHashMap.class);

//        System.out.println("Is data empty? "+ (dataMap.isEmpty() ? "jah!" : "nein!"));

        for(String member : dataMap.keySet()){
            payingMembers.put(member, Double.valueOf(dataMap.get(member)));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        double cost = Double.valueOf(expence.getString("cost")).doubleValue();
        //-----data conversion end---------------



        //should substitute this last step by using a DTO object
        CKExpenceBuilder ckeb = new CKExpenceBuilder();
        Group g= gs.findGroupByGroupName(expence.getString("groupName")).get();
        String groupOwner = g.getGroupOwner().getUsername();
        MExpence me = ckeb
                .date(sdf.parse(expence.getString("date")))
                .description(expence.getString("description"))
                .cost(cost)
                .payingMembers(payingMembers, groupOwner)
                .groupName(expence.getString("groupName")).buildM();

        return ms.createExpence(ckeb, me, groupOwner);
    }
//da proteggere con ruolo admin
    @GetMapping("/{expenceId}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public void withdrawFromChest(@PathVariable String expenceId, @RequestBody String user) throws ChestNotOpenedException, UserNotOwnerException, WrongExpenceTypeException {
        MExpence me = null;
        Optional<MExpence> result;
        if ((result = ms.findMExpenceById(expenceId)).isPresent()) {
            me = result.get();
            Chest chest = me.getChest();
            try {
                ms.withdraw(me, chest, user);
            } catch (UserNotOwnerException e) {
                throw e;
            } catch (ChestNotOpenedException e) {
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
        MExpence me;
        Optional<MExpence> result;
        if ((result = ms.findMExpenceById(expenceId)).isPresent()) {
            me = result.get();
            //check that this member is a debtor
            try {
                ms.deposit(me, me.getChest(), username);
            }catch(UserNotDebtorException e){
                throw e;
            }catch(ChestOpenedException e){
                throw e;
            }
        }
        else{
            throw new WrongExpenceTypeException();
        }
    }
}
