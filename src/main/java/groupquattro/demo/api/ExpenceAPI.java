package groupquattro.demo.api;

import groupquattro.demo.classes.Expence;
import groupquattro.demo.services.ExpenceService;
import org.bson.Document;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/expences")
public class ExpenceAPI {

    @Autowired
    private ExpenceService es;

    @GetMapping
    public ResponseEntity<List<Expence>> getAllExpences(){
        return new ResponseEntity<List<Expence>>(es.allExpence(), HttpStatus.OK);
    }

    @GetMapping("/{idPurchase}")
    public ResponseEntity<Optional<Expence>> getExpenceById(@PathVariable String idExpence){
        return new ResponseEntity<Optional<Expence>>(es.findExpenceByIdExpence(idExpence), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Expence> createExpence(@RequestBody Document expence){
        return new ResponseEntity<Expence>(es.createExpence(expence.getString("idExpence"), expence.getString("description"), expence.getDouble("cost"), expence.getString("idGroup")), HttpStatus.CREATED);
    }
}
