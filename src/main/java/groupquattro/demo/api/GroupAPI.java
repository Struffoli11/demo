package groupquattro.demo.api;

import groupquattro.demo.dto.GroupFormDto;
import groupquattro.demo.dto.GroupInfoDto;
import groupquattro.demo.dto.GroupPageDto;
import groupquattro.demo.dto.UserDto;
import groupquattro.demo.exceptions.DuplicateResourceException;
import groupquattro.demo.exceptions.ResourceNotFoundException;
import groupquattro.demo.exceptions.UserNotFoundException;
import groupquattro.demo.mapper.GroupInfoMapper;
import groupquattro.demo.services.GroupService;
import groupquattro.demo.services.UserService;
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

//    @GetMapping("/{idGroup}")
//    public ResponseEntity<?> findGroupById(@PathVariable String idGroup) throws ResourceNotFoundException {
//        GroupPageDto groupInfo = groupService.findGroupById(idGroup);
//        return ResponseEntity.ok(groupInfo);
//    }

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
            GroupPageDto groupPageDto = groupService.createGroup(formDto);
            return ResponseEntity.ok(groupPageDto);
        }catch (DuplicateResourceException e){
            return ResponseEntity.status(405).body(e.getMessage());
        }
    }

    @PutMapping("/{idGroup}/")
    public ResponseEntity<?> updateGroup(@RequestBody GroupPageDto groupPageDto) throws ResourceNotFoundException {
        GroupPageDto updatedGroup = groupService.updateGroup(groupPageDto);
        return ResponseEntity.ok(updatedGroup);
    }
}
