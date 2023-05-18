package groupquattro.demo.services;

import groupquattro.demo.classes.Debt;
import groupquattro.demo.repos.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DebtService {

    @Autowired
    private DebtRepository dr;

    public List<Debt> allDebts(){
        return dr.findAll();
    }

    public Optional<Debt> getDebt(String idDebt){
        return dr.findDebtByIdDebt(idDebt);
    }

    public Optional<Debt> createDebt(String idDebt, double value){
        if(getDebt(idDebt).isPresent()) return Optional.empty();
        return Optional.of(dr.insert(new Debt(idDebt, value)));
    }
}
