package groupquattro.demo.api;

import groupquattro.demo.classes.User;
import groupquattro.demo.services.UserService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserAPI {
    @Autowired
    UserService us;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<List<User>>(us.allUsers(), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable String username){
        return new ResponseEntity<Optional<User>>(us.findUserByUsername(username), HttpStatus.OK);
    }

    /**
     * checks that username is not already assigned to another user
     * @param userData a map object containing the username and password
     * @return the User created if username is unique into the database, otherwise returns null and does not update db
     */
    @PostMapping
    public ResponseEntity<Optional<User>> createUser(@RequestBody Map<String, String> userData){
        Optional<User> res = us.createUser(userData.get("username"), userData.get("password"));
        if(res.isPresent()) return new ResponseEntity<Optional<User>>(res, HttpStatus.CREATED);
        return new ResponseEntity<Optional<User>>(res, HttpStatus.BAD_REQUEST);
    }

}
