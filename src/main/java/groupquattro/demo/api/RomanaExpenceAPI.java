package groupquattro.demo.api;

import groupquattro.demo.exceptions.ParsingExpenceException;
import groupquattro.demo.exceptions.WrongExpenceTypeException;
import groupquattro.demo.model.Expence;
import groupquattro.demo.model.RomanaExpence;
import groupquattro.demo.services.RomanaExpenceService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/expences")
public class RomanaExpenceAPI {

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
        RomanaExpence ex = RomanaExpence.builder()
                .date(sdf.parse(expence.getString("date")))
                .description(expence.getString("description"))
                .cost(expence.getDouble("cost"))
                .payingMembers(payingMembers)
                .computeDebts().build();
                return es.createExpence(ex, expence.getString("idGroup"));
        }

}
