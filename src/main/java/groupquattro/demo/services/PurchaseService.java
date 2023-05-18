package groupquattro.demo.services;

import groupquattro.demo.classes.Purchase;
import groupquattro.demo.repos.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {
    @Autowired
    private PurchaseRepository pr;

    public List<Purchase> allPurchase(){
        return pr.findAll();
    }
}
