package groupquattro.demo.api;

import groupquattro.demo.exceptions.UserAlreadyAMemberException;
import groupquattro.demo.exceptions.UserNotFoundException;
import groupquattro.demo.model.Group;
import groupquattro.demo.model.User;
import groupquattro.demo.repos.UserRepository;
import groupquattro.demo.services.GroupService;
import groupquattro.demo.services.UserService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/groups")
public class GroupAPI {
    @Autowired
    private UserService us;

    @Autowired
    private GroupService gs;
    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups(){
        return new ResponseEntity<List<Group>>(gs.allGroups(), HttpStatus.OK);
    }

    @GetMapping("/{groupName}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Group> getGroup(@PathVariable String groupName){
        return gs.findGroupByGroupName(groupName);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Group createGroup(@RequestBody Document group) throws UserNotFoundException {
        String groupName = group.getString("groupName");
        String groupOwner = group.getString("groupOwner");
        String emailOwner = group.getString("emailOwner");
        List<String> members = new ArrayList<String>(group.getList("members", String.class));
        Optional<User> groupOwnerUser = us.findUserByUsernameAndEmail(groupOwner, emailOwner);
        if(groupOwnerUser.isPresent()) {
            if (members.isEmpty()) {
                return gs.createGroup(groupName, groupOwnerUser.get());
            }
            return gs.createGroup(groupName, groupOwnerUser.get(), members);

        }
        else {
            throw new UserNotFoundException("GroupOwner does not exist within db.");
        }
    }

    @PostMapping("/{groupName}")
    @ResponseStatus(HttpStatus.OK)
    public List<String> addNewMember(@PathVariable String groupName, @RequestBody Map<String, String> userData) throws UserNotFoundException {
        String username = userData.get("username");
        String email = userData.get("email");
        Optional<User> user = us.findUserByUsernameAndEmail(username, email);
        System.out.println(user.get().getUsername() + " " + user.get().getEmail());
        if(user.isPresent()){
            System.out.println("HELLO THERE");
            Optional<Group> g = gs.addMemberToGroup(groupName, user.get());
            if(g.isPresent()){
                System.out.println(g.get().getGroupName());
                return g.get().getMembers();
            }

        }
        throw  new UserNotFoundException("user not found.");
    }
}
