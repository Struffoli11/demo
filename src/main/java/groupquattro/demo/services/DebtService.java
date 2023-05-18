package groupquattro.demo.services;

import groupquattro.demo.classes.Debt;
import groupquattro.demo.repos.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtService {

    @Autowired
    private DebtRepository dr;

    public List<Debt> allDebts(){
        return dr.findAll();
    }
}
