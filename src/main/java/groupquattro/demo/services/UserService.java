package groupquattro.demo.services;

import groupquattro.demo.dto.DebtDto;
import groupquattro.demo.dto.GroupPageDto;
import groupquattro.demo.dto.RegisterRequestDto;
import groupquattro.demo.dto.UserDto;
import groupquattro.demo.exceptions.DuplicateResourceException;
import groupquattro.demo.exceptions.ResourceNotFoundException;
import groupquattro.demo.model.Debt;
import groupquattro.demo.model.Group;
import groupquattro.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<UserDto> getAllUsers() throws ResourceNotFoundException;

    public UserDto findUserByUsername(String username) throws ResourceNotFoundException;

    public UserDto createUser(RegisterRequestDto userDto) throws DuplicateResourceException;


    public void updateUserKeys(String  keyId, String username) throws ResourceNotFoundException;

    void deleteUserDebt(DebtDto debtDto, String username);

    void removeKey(String id, String chestUser);

    void addGroup(GroupPageDto group, String groupMember) throws ResourceNotFoundException;

    void saveUser(User user);

    void createDebt(Debt debt, String debtorUsername);
}
