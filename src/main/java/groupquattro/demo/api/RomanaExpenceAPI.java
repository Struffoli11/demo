package groupquattro.demo.api;

import groupquattro.demo.exceptions.WrongExpenceTypeException;
import groupquattro.demo.model.Expence;
import groupquattro.demo.model.Group;
import groupquattro.demo.model.RomanaExpence;
import groupquattro.demo.model.RomanaExpenceBuilder;
import groupquattro.demo.services.GroupService;
import groupquattro.demo.services.RomanaExpenceService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/expences")
public class RomanaExpenceAPI {
    @Autowired
    private GroupService gs;

    @Autowired
    private RomanaExpenceService es;

    @GetMapping
    public ResponseEntity<List<RomanaExpence>> getAllExpences(){
        return new ResponseEntity<List<RomanaExpence>>(es.allRomanaExpences(), HttpStatus.OK);
    }

    @GetMapping("/{idExpence}")
    public ResponseEntity<Optional<RomanaExpence>> getExpenceById(@PathVariable String idExpence){
        return new ResponseEntity<Optional<RomanaExpence>>(es.findExpenceByIdExpence(idExpence), HttpStatus.OK);
    }

    @GetMapping("/{groupName}/group-expences")
    public List<RomanaExpence> getExpencesByDescription(@PathVariable String groupName, @RequestBody Map<String, String> query) {
        Optional<Group> g = gs.findGroupByGroupName(groupName);
        List<RomanaExpence> expencesMatchingGroup = es.findByGroupName(groupName);
        for(RomanaExpence re : expencesMatchingGroup){
            System.out.println(re);
        }
        System.out.println(query.get("description"));
        List<RomanaExpence> expencesMatchingGroupAndDescritpion = new ArrayList<>();
        for(RomanaExpence re : expencesMatchingGroup){
            if(re.getDescription().equals(query.get("description")))
                expencesMatchingGroupAndDescritpion.add(re);
        }
        return expencesMatchingGroupAndDescritpion;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RomanaExpence createExpence(@RequestBody Document expence) throws ParseException, WrongExpenceTypeException {
        Map<String, Double> payingMembers = expence.get("payingMembers", LinkedHashMap.class);
//        for(String member : payingMembers.keySet()){
//            System.out.println(payingMembers.get(member));
//            System.out.println(member);
//        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date d;
        RomanaExpence ex = new RomanaExpenceBuilder()
                .date(sdf.parse(expence.getString("date")))
                .description(expence.getString("description"))
                .cost(expence.getDouble("cost"))
                .payingMembers(payingMembers)
                .groupName(expence.getString("groupName"))
                .computeDebts().build();
                return es.createExpence(ex);
        }

}
