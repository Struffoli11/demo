package groupquattro.demo.services;

import groupquattro.demo.classes.Group;
import groupquattro.demo.classes.Expence;
import groupquattro.demo.repos.ExpenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenceService {
    @Autowired
    private MongoTemplate mt;
    @Autowired
    private ExpenceRepository er;

    public List<Expence> allExpence(){
        return er.findAll();
    }

    public Optional<Expence> findExpenceByIdExpence(String idExpence){
        return er.findExpenceByIdExpence(idExpence);
    }

    public Expence createExpence(String idExpence, String description, double cost, String idGroup){
        Expence aExpence = er.insert(new Expence(cost, idExpence, description));

        mt.update(Group.class)
                .matching(Criteria.where("idGroup").is(idGroup))
                .apply(new Update().push("expences").value(aExpence)).first();

        return aExpence;
    }

}
