package groupquattro.demo.services;

import groupquattro.demo.model.Group;
import groupquattro.demo.model.Debt;
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

    public List<RomanaExpence> findByGroupName(String groupName){
        return er.findRomanaExpenceByGroupName(groupName);
    }


    public RomanaExpence createExpence(RomanaExpence ex) {

        List<Debt> trueDebts = new ArrayList<Debt>();
        if(ex.getDebts()!=null) {
            for (Debt debt : ex.getDebts()) {
                debt = dr.insert(new Debt(debt.getDebt(), debt.getCreditor(), debt.getDebtor()));
                trueDebts.add(debt);
            }
            ex.setDebts(trueDebts);
        }
        ex = er.insert(ex);
        mt.update(Group.class)
                .matching(Criteria.where("groupName").is(ex.getGroupName()))
                .apply(new Update().push("expences").value(ex)).first();
        return ex;
    }


}
