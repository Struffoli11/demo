package groupquattro.demo.services;

import groupquattro.demo.dto.DebtDto;
import groupquattro.demo.dto.GroupPageDto;
import groupquattro.demo.dto.RegisterRequestDto;
import groupquattro.demo.dto.UserDto;
import groupquattro.demo.exceptions.DuplicateResourceException;
import groupquattro.demo.exceptions.ResourceNotFoundException;
import groupquattro.demo.mapper.UserDtoMapper;
import groupquattro.demo.mapper.UserDtoMapperImpl;
import groupquattro.demo.mapper.UserRegistrationMapper;
import groupquattro.demo.model.*;
import groupquattro.demo.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    private UserDtoMapperImpl userDtoMapper = new UserDtoMapperImpl();

    @Autowired
    private UserRegistrationMapper userRegistrationMapper;

    @Override
    public List<UserDto> getAllUsers() throws ResourceNotFoundException {
        List<User> userList = userRepo.findAll();
        if(userList.isEmpty()){
            throw new ResourceNotFoundException("users list not found");
        }
        else{
            return userDtoMapper.convertUserListToUserDtoList(userList);
        }
    }

    @Override
    public UserDto findUserByUsername(String username) throws ResourceNotFoundException {
        Optional<User> user = userRepo.findUserByUsername(username);
        if(!user.isPresent()){
            throw new ResourceNotFoundException("user "+ username + " not found");
        }
        else{
            return userDtoMapper.toDto(user.get());
        }
    }

    @Override
    public UserDto createUser(RegisterRequestDto userDto) throws DuplicateResourceException {
        User newUser = userRegistrationMapper.toModel(userDto);
        newUser.setDebts(new ArrayList<Debt>());
        newUser.setGroups(new ArrayList<Group>());
        newUser.setKeys(new ArrayList<String>());
        newUser.setPassword(newUser.getPassword());
        UserDto savedUserDto = null;
        User savedUser = null;
        if(!userRepo.findUserByUsername(userDto.getUsername()).isPresent()) {
            if(!userRepo.findUserByEmail(userDto.getEmail()).isPresent()) {
                savedUser = userRepo.save(newUser);
                savedUserDto = userDtoMapper.toDto(savedUser);
                return savedUserDto;
            }
            else{
                throw new DuplicateResourceException("email " + userDto.getEmail() + " already in use");
            }
        }
        else{
            throw new DuplicateResourceException("username " + userDto.getUsername() + " is already in  use.");
        }
    }


    @Override
    public void updateUserKeys(String keyId, String username) throws ResourceNotFoundException {
        Optional<User> user = userRepo.findUserByUsername(username);
        if(!user.isPresent()){
            throw new ResourceNotFoundException("user "+ username+ " not found");
        }
        else{
            User aUser = user.get();
            aUser.getKeys().add(keyId);
            return;
        }
    }

    @Override
    public void deleteUserDebt(DebtDto debtDto, String username) {
        Optional<User> optionalUser = userRepo.findUserByUsername(username);
        if(optionalUser.isPresent()){
            UserDto userDto = userDtoMapper.toDto(optionalUser.get());
            userDto.getDebts().remove(debtDto);
            User updatedUser = userDtoMapper.updateModel(userDto, optionalUser.get());
            userRepo.save(updatedUser);
        }
    }

    @Override
    public void removeKey(String id, String chestUser) {
        Optional<User> aUser = userRepo.findUserByUsername(chestUser);
        if(aUser.isPresent()){
            UserDto userDto = userDtoMapper.toDto(aUser.get());
            userDto.getKeys().remove(id);
            User updatedUser = userDtoMapper.updateModel(userDto, aUser.get());
            userRepo.save(updatedUser);
        }
    }

    @Override
    public void addGroup(GroupPageDto group, String groupMember) throws ResourceNotFoundException {
        Optional<User> aUser = userRepo.findUserByUsername(groupMember);
        if(aUser.isPresent()){
            UserDto userDto = userDtoMapper.toDto(aUser.get());
            if(userDto.getGroups().isEmpty()){
                userDto.setGroups(new ArrayList<GroupPageDto>());
            }
            userDto.getGroups().add(group);
            User updatedUser = userDtoMapper.updateModel(userDto, aUser.get());
            userRepo.save(updatedUser);
        }
        else{
            throw new ResourceNotFoundException("user " + groupMember + " not found");
        }
    }

    @Override
    public void saveUser(User user) {

    }

    @Override
    public void createDebt(Debt debt, String debtorUsername) {
        User aUser = userRepo.findUserByUsername(debtorUsername).orElseThrow();
        aUser.getDebts().add(debt);
        userRepo.save(aUser);
        return;
    }
//
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<groupquattro.demo.model.User> optionalUser = userRepo.findUserByUsername(username);
//        if(optionalUser.isPresent()){
//            groupquattro.demo.model.User user = optionalUser.get();
//            org.springframework.security.core.userdetails.UserDetails user1 = org.springframework.security.core.userdetails.User.builder()
//                    .username(user.getUsername())
//                    .password(user.getPassword())
//                    .authorities(mapRolesToAuthorities(user.getRoles()))
//                    .build();
//
//            user1.
//        }else{
//            throw new UsernameNotFoundException("user " + username + " not found");
//        }
//    }
//
//    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
//        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
//    }
}
