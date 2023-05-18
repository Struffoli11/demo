package groupquattro.demo.api;

import groupquattro.demo.classes.Purchase;
import groupquattro.demo.services.PurchaseService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/purchase")
public class PurchaseAPI {

    @Autowired
    private PurchaseService ps;

    @GetMapping
    public ResponseEntity<List<Purchase>> getAllPurchases(){
        return new ResponseEntity<List<Purchase>>(ps.allPurchase(), HttpStatus.OK);
    }
}
