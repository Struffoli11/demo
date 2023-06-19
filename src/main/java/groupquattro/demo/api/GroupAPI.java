package groupquattro.demo.api;

import groupquattro.demo.dto.GroupFormDto;
import groupquattro.demo.dto.GroupInfoDto;
import groupquattro.demo.dto.GroupPageDto;
import groupquattro.demo.dto.UserDto;
import groupquattro.demo.exceptions.DuplicateResourceException;
import groupquattro.demo.exceptions.ResourceNotFoundException;
import groupquattro.demo.exceptions.UserAlreadyAMemberException;
import groupquattro.demo.exceptions.UserNotFoundException;
import groupquattro.demo.mapper.GroupInfoMapper;
import groupquattro.demo.services.GroupService;
import groupquattro.demo.services.UserService;
import jakarta.ws.rs.HEAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupAPI {
    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupInfoMapper groupInfoMapper;

    @GetMapping
    public ResponseEntity<?> getAllGroups() throws ResourceNotFoundException {
        List<GroupInfoDto> groupList = groupService.getAllGroups();
        return ResponseEntity.ok(groupList);
    }

//    @GetMapping("/{idGroup}/expences")
//    public ResponseEntity<?> getAllExpencesByDescription(@PathVariable String idGroup) throws ResourceNotFoundException {
//        GroupPageDto groupPageDto = groupService.findGroupById(idGroup);
//        //select the right expences
//        return ResponseEntity.ok(groupPageDto.getExpences());
//    }

    @PostMapping("/{idGroup}/join")
    public ResponseEntity<?> addUserToGroup(@PathVariable String idGroup) throws ResourceNotFoundException, UserAlreadyAMemberException {
        try{
            GroupPageDto group = groupService.findGroupById(idGroup);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            GroupPageDto updatedPage = groupService.addUser(group, username, idGroup);
            return ResponseEntity.ok().body("User " + username + "has been added to this group.");
        }catch (ResourceNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
            return ResponseEntity.status(405).body(e.getLocalizedMessage());
        } catch (UserAlreadyAMemberException e) {
            System.err.println(e.getLocalizedMessage());
            return ResponseEntity.status(405).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/{groupName}")
    public ResponseEntity<?> findGroupByGroupName(@PathVariable String groupName) throws ResourceNotFoundException {
        GroupPageDto groupPage = groupService.findGroupByGroupName(groupName);//in this object, expences are visible
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDto aUser = userService.findUserByUsername(username);
        for(GroupPageDto groupPageDto : aUser.getGroups()){
            if(groupPageDto.getGroupName().equals(groupName)){
                return ResponseEntity.ok(groupPage);
            }
        }
        return ResponseEntity.ok(groupInfoMapper.fromGroupPageDtoToGroupInfoDto(groupPage));
    }
    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody GroupFormDto formDto) throws UserNotFoundException, DuplicateResourceException {
        try{
            String groupOwner = formDto.getGroupOwner();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            if(groupOwner.equals(username)) {
                GroupPageDto groupPageDto = groupService.createGroup(formDto);
                return ResponseEntity.ok(groupPageDto);
            }else {
                return ResponseEntity.status(401).body("You are not logged in as " + groupOwner);
            }
        }catch (DuplicateResourceException e){
            return ResponseEntity.status(405).body(e.getMessage());
        }
    }

    @PutMapping("/{idGroup}/")
    public ResponseEntity<?> updateGroup(@RequestBody GroupPageDto groupPageDto) throws ResourceNotFoundException {
        String groupOwner = groupPageDto.getGroupOwner();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(groupOwner.equals(username)) {
            GroupPageDto updatedGroup = groupService.updateGroup(groupPageDto);
            return ResponseEntity.ok(updatedGroup);
        }
        else{
            //only the GroupOwner can update Group Details such as GroupName or GroupOwner
            return ResponseEntity.status(401).body("Please to update Group Info log in as Group Owner: " + groupOwner);
        }
    }

}
