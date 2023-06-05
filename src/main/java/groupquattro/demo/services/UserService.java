package groupquattro.demo.services;

import groupquattro.demo.model.Group;
import groupquattro.demo.model.User;
import groupquattro.demo.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    public UserService(){}
    @Autowired
    private MongoTemplate mt;
    @Autowired
    private UserRepository ur;

    public List<User> allUsers(){
        return ur.findAll();
    }

    public Optional<User> findUserByUsernameAndEmail(String username, String email){
        return ur.findUserByUsernameAndEmail(username, email);
    }

    public Optional<User> findUserByUsername(String username){
        return ur.findUserByUsername(username);
    }

    public User createUser(String username, String email) {
        User user =  ur.insert(new User(username, email));
        return user;
    }


}
