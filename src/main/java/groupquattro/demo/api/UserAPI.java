package groupquattro.demo.api;

import groupquattro.demo.dto.GroupPageDto;
import groupquattro.demo.dto.RegisterRequestDto;
import groupquattro.demo.dto.UserDto;
import groupquattro.demo.exceptions.DuplicateResourceException;
import groupquattro.demo.exceptions.ResourceNotFoundException;
import groupquattro.demo.exceptions.UserAlreadyAMemberException;
import groupquattro.demo.mapper.UserInfoMapper;
import groupquattro.demo.services.GroupService;
import groupquattro.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users/")
public class UserAPI {
    @Autowired
    UserService userService;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    GroupService groupService;

    @GetMapping
    public ResponseEntity<?> findAll() throws ResourceNotFoundException {
        List<UserDto> userDtoList = userService.getAllUsers();
        return ResponseEntity.ok(userInfoMapper.toListUserInfo(userDtoList));
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable("username") String username) throws ResourceNotFoundException {
        UserDto userDto = userService.findUserByUsername(username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = authentication.getName();
        if(principal.equals(username)){
            return ResponseEntity.ok(userDto);
        }//just user info (email and username)
        userInfoMapper.toInfo(userDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody RegisterRequestDto userDto) throws DuplicateResourceException {
        try {
            UserDto createdUser = userService.createUser(userDto);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{username}")
                    .buildAndExpand(createdUser.getUsername())
                    .toUri();
            return ResponseEntity.created(uri)
                    .body(createdUser);
        }catch(DuplicateResourceException e){
            System.err.println(e.getLocalizedMessage());
            return ResponseEntity.status(405).body(e.getLocalizedMessage());
        }
    }

//    @PostMapping("joinGroup/{groupName}")
//    public ResponseEntity<?> joinGroup(@PathVariable("groupName") String groupName,
//                                       @RequestBody String groupId){
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String username = authentication.getName();
//            GroupPageDto groupPage = groupService.findGroupById(groupId);
//            GroupPageDto updatedPage = groupService.addUser(groupPage, username, groupId);
//            return ResponseEntity.ok(updatedPage);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(405).body(e.getLocalizedMessage());
//        } catch (UserAlreadyAMemberException e) {
//            return ResponseEntity.status(405).body(e.getLocalizedMessage());
//        }
//    }

//    @GetMapping("/{username}/groups")
//    @ResponseStatus(HttpStatus.OK)
//    public List<Group> getUserGroups(@PathVariable String username){
//        Optional<User> aUser = us.findUserByUsername(username);
//        if(aUser.isPresent()){
//            return aUser.get().getGroups();
//        }
//        return null;
//    }
}
