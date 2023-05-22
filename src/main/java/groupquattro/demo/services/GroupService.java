package groupquattro.demo.services;

import com.mongodb.client.result.UpdateResult;
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
    public GroupService(){}
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

    public Optional<Group> addMemberToGroup(String groupName, User user){
        UpdateResult res = mt.update(Group.class)
                .matching(Criteria.where("groupName").is(groupName))
                .apply(new Update().push("members").value(user.getUsername())).first();

        System.out.println("found matches #" + res.getMatchedCount()+ " and modified: " + res.getModifiedCount());

        res = mt.update(User.class)
                .matching(Criteria.where("email").is(user.getEmail()))
                .apply(new Update().push("groups").value(groupName)).first();
        System.out.println("found matches #" + res.getMatchedCount()+ " and modified: " + res.getModifiedCount());
        return findGroupByGroupName(groupName);
    }

    public Optional<Group> addMemberToGroup(String groupName, String username, String email){
        UpdateResult res = mt.update(Group.class)
                .matching(Criteria.where("groupName").is(groupName))
                .apply(new Update().push("members").value(username)).first();

        System.out.println("found matches #" + res.getMatchedCount()+ " and modified: " + res.getModifiedCount());

        res = mt.update(User.class)
                .matching(Criteria.where("email").is(email))
                .apply(new Update().push("groups").value(groupName)).first();
        System.out.println("found matches #" + res.getMatchedCount()+ " and modified: " + res.getModifiedCount());

        return findGroupByGroupName(groupName);
    }

    public List<String> getGroupMembers(String groupName){
        Optional<Group> aGroup = findGroupByGroupName(groupName);
        return aGroup.orElse(null).getMembers();
    }

    public Optional<Group> findGroupByGroupName(String groupName){
        return gr.findGroupByGroupName(groupName);
    }

    public Group createGroup(String groupName, User groupOwner, List<String> members ){
        Group aGroup = gr.insert(new Group(groupName, groupOwner, members));

        for(String username : members){
            System.out.printf("User: "+ username);
            mt.update(User.class)
                    .matching(Criteria.where("username").is(username))
                    .apply(new Update().push("groups").value(aGroup)).first();
        }

        return aGroup;
    }

    public Group createGroup(String groupName, User groupOwner){
        Group aGroup = gr.insert(new Group(groupName, groupOwner));
        mt.update(User.class)
                .matching(Criteria.where("username").is(groupOwner.getUsername()))
                .apply(new Update().push("groups").value(aGroup.getGroupName())).first();

        return aGroup;
    }
}
