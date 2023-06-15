package groupquattro.demo.services;

import groupquattro.demo.dto.ChestDto;
import groupquattro.demo.exceptions.*;
import groupquattro.demo.model.Chest;

import java.util.List;

public interface ChestService {

    public List<ChestDto> getAllChests() throws ResourceNotFoundException;

    public ChestDto findChestById(String id) throws ResourceNotFoundException;

    Chest createChest(Chest chest) throws ResourceNotFoundException;

    boolean withdraw(String chestId, String chestUser) throws UserNotOwnerException, ChestNotOpenedException, ResourceNotFoundException, UserUpdateException;

    void deposit(String chestId, double amount) throws ChestOpenedException, ResourceNotFoundException;
}
