package groupquattro.demo.services;

import groupquattro.demo.classes.User;
import groupquattro.demo.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private MongoTemplate mt;
    @Autowired
    private UserRepository ur;
    public List<User> allUsers(){
        return ur.findAll();
    }

    public Optional<User> findUserByUsername(String username){
        return ur.findUserByUsername(username);
    }

    public Optional<User> createUser(String username, String password) {
        if(ur.findUserByUsername(username).isPresent()){
            return Optional.empty();
        }

        User user =  ur.insert(new User(username, password));
        return Optional.of(user);
    }

}
