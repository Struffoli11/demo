package groupquattro.demo.services;

import groupquattro.demo.classes.Group;
import groupquattro.demo.repos.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    private GroupRepository gr;

    public List<Group> allGroups(){
        return gr.findAll();
    }

    public Optional<Group> findGroupById(String idGroup){
        return gr.findGroupByIdGroup(idGroup);
    }
}
