package groupquattro.demo.services;

import groupquattro.demo.dto.ChestDto;
import groupquattro.demo.dto.UserDto;
import groupquattro.demo.exceptions.*;
import groupquattro.demo.mapper.ChestMapper;
import groupquattro.demo.mapper.ChestMapperImpl;
import groupquattro.demo.model.Chest;
import groupquattro.demo.model.Key;
import groupquattro.demo.repos.ChestRepository;
import groupquattro.demo.repos.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChestServiceImpl implements ChestService {

    @Autowired
    private MongoTemplate mt;

    @Autowired
    private KeyRepository keyRepository;

    @Autowired
    private UserService userService;
    private ChestMapper chestMapper = new ChestMapperImpl();

    @Autowired
    private ChestRepository chestRepository;


    @Override
    public List<ChestDto> getAllChests() throws ResourceNotFoundException {
        List<Chest> chestList = chestRepository.findAll();
        if (chestList.isEmpty()) {
            throw new ResourceNotFoundException("chestList not found");
        } else {
            return chestMapper.convertChestListToDtoList(chestList);
        }
    }

    @Override
    public ChestDto findChestById(String id) throws ResourceNotFoundException {
        Optional<Chest> chest = chestRepository.findById(id);
        if (chest.isPresent()) {
            return chestMapper.toDto(chest.get());
        } else {
            throw new ResourceNotFoundException("chest " + id + " not found");
        }
    }

    @Override
    public Chest createChest(Chest chest) throws ResourceNotFoundException {
        Key key = chest.getChestKey();
        Key savedKey = keyRepository.insert(key);//make the key persistent
        chest.setChestKey(savedKey);
         for(String owner : savedKey.getListOfOwners().keySet()) {
             UserDto userDto = userService.findUserByUsername(owner);//the user gets the id of the key owned
             if(userDto.getKeys().isEmpty()){
                 userDto.setKeys(new ArrayList<String>());
             }
             userDto.getKeys().add(savedKey.getId());
             userService.updateUserKeys(savedKey.getId(), userDto.getUsername());
         }
        Chest savedChest = chestRepository.save(chest);
        return savedChest;
    }

//    public Chest updateChest(Chest chest, CKExpence cke) {
//        mt.save(chest.getChestKey());
//        mt.save(chest);
//        mt.save(cke);
//        return chest;
//    }

    @Override
    public boolean withdraw(String chestId, String chestUser) throws UserNotOwnerException, ChestNotOpenedException, ResourceNotFoundException, UserUpdateException {
        Optional<Chest> optionalChest = chestRepository.findById(chestId);
        if (optionalChest.isPresent()) {
            Chest chest = optionalChest.get();
            if (chest.isOpen()) {
                Key key = chest.getChestKey();
                double valueToWithdraw = accessOwnerList(key, chestUser);
                chest.withdraw(valueToWithdraw);
                if(keyRepository.findById(chest.getChestKey().getId()).isPresent()){
                    chestRepository.save(chest);
                }
                else{
                    chestRepository.delete(chest);
                }
                return true;
            }else{
                throw new ChestNotOpenedException("chest " + chestId + " is not opened");
            }
        }
        else {
            throw new ResourceNotFoundException("chest " + chestId + " not found");
        }
    }

    private double accessOwnerList(Key key, String chestUser) throws UserNotOwnerException {
        for(String owner : key.getListOfOwners().keySet()){
            if(owner.equals(chestUser)){
                double value =  key.getListOfOwners().get(owner);
                userService.removeKey(key.getId(), chestUser);//update user keyList
                key.getListOfOwners().remove(owner);//update the keyOwnerList
                if(key.getListOfOwners().isEmpty()){
                    keyRepository.delete(key);
                }else {
                    keyRepository.save(key);
                }
                return value;
            }
        }
        throw new UserNotOwnerException("user "+ chestUser + " is not an owner");
    }

    /**
     * Members can deposit only when the chest is still closed
     * which is, when current value inside chest is strictly less than
     * the max amount, which is in turn the amount of money that "opens" the chest
     * @param chestId
     * @return
     * @throws ChestOpenedException
     * @throws UserNotDebtorException
     */
    @Override
    public void deposit(String chestId, double amount) throws ChestOpenedException, ResourceNotFoundException {
        Optional<Chest> optionalChest = chestRepository.findById(chestId);
        if (optionalChest.isPresent()) {
            Chest chest = optionalChest.get();
            if (!chest.isOpen()){
                    double newValue = chest.deposit(amount);
                    if (newValue>=chest.getMax_amount()) {
                        chest.setOpen(true);
                        chestRepository.save(chest);
                    } else {
                        chestRepository.save(chest);
                    }
            }else {
                throw new ResourceNotFoundException("chest " + chestId + " not found");
            }
        }else {
            throw new ChestOpenedException("chest "+chestId+ " is alredy opened");
        }

    }
}
