package groupquattro.demo.api;

import groupquattro.demo.model.Wallet;
import groupquattro.demo.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletAPI {
    @Autowired
    private WalletService ws;

    @GetMapping
    public ResponseEntity<List<Wallet>> getAllWallets(){
        return new ResponseEntity<List<Wallet>>(ws.allWallets(), HttpStatus.OK);
    }
}
