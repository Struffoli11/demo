package groupquattro.demo.api;

import groupquattro.demo.exceptions.UserNotFoundException;
import groupquattro.demo.model.Group;
import groupquattro.demo.services.GroupService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups")
public class GroupAPI {
    @Autowired
    UserAPI userAPI;
    @Autowired
    private GroupService gs;
    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups(){
        return new ResponseEntity<List<Group>>(gs.allGroups(), HttpStatus.OK);
    }

    @GetMapping("/{idGroup}")
    public ResponseEntity<Optional<Group>> getGroup(@PathVariable String idGroup){
        return new ResponseEntity<Optional<Group>>(gs.findGroupById(idGroup), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Group> createGroup(@RequestBody Document group) throws UserNotFoundException {
        String groupName = group.getString("groupName");
        String idGroup = group.getString("idGroup");
        List<String> members = group.getList("members" ,String.class);

        return new ResponseEntity<Group>(gs.createGroup(groupName, idGroup, members), HttpStatus.CREATED);
    }
}
