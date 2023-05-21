package groupquattro.demo.services;

import groupquattro.demo.model.Group;
import groupquattro.demo.model.RomanaExpence;
import groupquattro.demo.repos.DebtRepository;
import groupquattro.demo.repos.RomanaExpenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RomanaExpenceService {
    @Autowired
    private MongoTemplate mt;
    @Autowired
    private RomanaExpenceRepository er;
    @Autowired
    private DebtRepository dr;

    public List<RomanaExpence> allRomanaExpences(){
        return er.findAll();
    }

    public Optional<RomanaExpence> findExpenceByIdExpence(String idExpence){
        return er.findRomanaExpenceById(idExpence);
    }

    public RomanaExpence createExpence(Date date, Map<String, Double> payingMembers, List<RomanaExpence.Debt> debts,
                                 String description, double cost, String idGroup){

        for(RomanaExpence.Debt debt : debts){
            System.out.println(debt);
            debt = dr.insert(new RomanaExpence.Debt(debt.getDebt(), debt.getCreditor(), debt.getDebtor()));
            System.out.println(debt);
        }

        RomanaExpence aExpence = er.insert(new RomanaExpence(cost, payingMembers, debts, description, date));

        mt.update(Group.class)
                .matching(Criteria.where("idGroup").is(idGroup))
                .apply(new Update().push("expences").value(aExpence)).first();

        return aExpence;
    }

    public RomanaExpence createExpence(RomanaExpence ex, String idGroup) {

        List<RomanaExpence.Debt> trueDebts = new ArrayList<RomanaExpence.Debt>();
        if(ex.getDebts()!=null) {
            for (RomanaExpence.Debt debt : ex.getDebts()) {
                debt = dr.insert(new RomanaExpence.Debt(debt.getDebt(), debt.getCreditor(), debt.getDebtor()));
//            System.out.println(debt);
                trueDebts.add(debt);
            }
            ex.setDebts(trueDebts);
        }

        ex = er.insert(ex);
        mt.update(Group.class)
                .matching(Criteria.where("idGroup").is(idGroup))
                .apply(new Update().push("expences").value(ex)).first();
        return ex;
    }
}
