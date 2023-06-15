package groupquattro.demo.services;

import groupquattro.demo.dto.DebtDto;
import groupquattro.demo.dto.GroupPageDto;
import groupquattro.demo.dto.RegisterRequestDto;
import groupquattro.demo.dto.UserDto;
import groupquattro.demo.exceptions.DuplicateResourceException;
import groupquattro.demo.exceptions.ResourceNotFoundException;

import java.util.List;

public interface UserService {

    public List<UserDto> getAllUsers() throws ResourceNotFoundException;

    public UserDto findUserByUsername(String username) throws ResourceNotFoundException;

    public UserDto createUser(RegisterRequestDto userDto) throws DuplicateResourceException;


    public UserDto updateUser(UserDto userDto) throws ResourceNotFoundException;

    void deleteUserDebt(DebtDto debtDto, String username);

    void removeKey(String id, String chestUser);

    void addGroup(GroupPageDto group, String groupMember) throws ResourceNotFoundException;
}
