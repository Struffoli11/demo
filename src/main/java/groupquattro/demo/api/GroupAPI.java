package groupquattro.demo.api;

import groupquattro.demo.classes.Group;
import groupquattro.demo.services.GroupService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupAPI {
    @Autowired
    private GroupService gs;
    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups(){
        return new ResponseEntity<List<Group>>(gs.allGroups(), HttpStatus.OK);
    }
}