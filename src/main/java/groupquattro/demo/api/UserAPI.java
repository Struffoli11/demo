package groupquattro.demo.api;

import groupquattro.demo.dto.GroupPageDto;
import groupquattro.demo.dto.RegisterRequestDto;
import groupquattro.demo.dto.ServerResponse;
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
    public ResponseEntity<?> findByUsername(@PathVariable("username") String username) {
        try {
            UserDto userDto = userService.findUserByUsername(username);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String principal = authentication.getName();
            if (principal.equals(username)) {
                return ResponseEntity.ok(userDto);
            }//just user info (email and username)
            userInfoMapper.toInfo(userDto);
            return ResponseEntity.ok(userDto);
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(404).body(new ServerResponse(e.getLocalizedMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody RegisterRequestDto userDto)  {
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
            return ResponseEntity.status(405).body(new ServerResponse(e.getLocalizedMessage()));
        }
    }
}
