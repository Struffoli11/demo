package groupquattro.demo.api;

import groupquattro.demo.classes.Debt;
import groupquattro.demo.services.DebtService;
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
@RequestMapping("debts")
public class DebtAPI {
    @Autowired
    private DebtService ds;

    @GetMapping
    public ResponseEntity<List<Debt>> getAllDebts(){
        return new ResponseEntity<List<Debt>>(ds.allDebts(), HttpStatus.OK);
    }
}
