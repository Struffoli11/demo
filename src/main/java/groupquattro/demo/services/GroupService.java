package groupquattro.demo.services;

import groupquattro.demo.model.Group;
import groupquattro.demo.model.User;
import groupquattro.demo.repos.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    MongoTemplate mt;
    @Autowired
    private GroupRepository gr;

    public List<Group> allGroups(){
        return gr.findAll();
    }

    public Optional<Group> findGroupById(String idGroup){
        return gr.findGroupByIdGroup(idGroup);
    }

    public Group createGroup(String groupName, String idGroup, List<String> members ){
        Group aGroup = gr.insert(new Group(groupName, idGroup, members));


        for(String username : members){
            System.out.printf("User: "+ username);
            mt.update(User.class)
                    .matching(Criteria.where("username").is(username))
                    .apply(new Update().push("groups").value(aGroup)).first();
        }

        return aGroup;
    }
}
