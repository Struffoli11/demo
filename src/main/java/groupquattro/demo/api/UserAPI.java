package groupquattro.demo.api;

import groupquattro.demo.exceptions.UserNotFoundException;
import groupquattro.demo.model.Group;
import groupquattro.demo.model.User;
import groupquattro.demo.services.GroupService;
import groupquattro.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserAPI {
    UserAPI(){}
    @Autowired
    UserService us;

    @Autowired
    GroupService gs;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<List<User>>(us.allUsers(), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getUser(@PathVariable String username){
        return us.findUserByUsername(username);
    }

    /**
     * checks that username is not already assigned to another user
     * @param userData a map object containing the username and password
     * @return the User created if username is unique into the database, otherwise returns null and does not update db
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody Map<String, String> userData){
        return us.createUser(userData.get("username"), userData.get("email"));
    }

    @PostMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Group joinGroup(@PathVariable String username, @RequestBody Map<String, String> data){
        Optional<User> aUser = us.findUserByUsernameAndEmail(username, data.get("email"));
        if(aUser.isPresent()){
            String email = aUser.get().getEmail();
            Optional<Group> aGroup = gs.addMemberToGroup(data.get("groupName"), username, email);
            return aGroup.orElse(null);
        }
        throw  new UserNotFoundException("user not found");
    }
}
