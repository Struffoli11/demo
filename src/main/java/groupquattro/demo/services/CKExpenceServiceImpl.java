package groupquattro.demo.services;

import groupquattro.demo.dto.CKExpenceFormDto;
import groupquattro.demo.dto.CKExpenceSummaryDto;
import groupquattro.demo.exceptions.*;
import groupquattro.demo.mapper.CKExpenceFormMapper;
import groupquattro.demo.mapper.CKExpenceFormMapperImpl;
import groupquattro.demo.mapper.CKExpenceMapper;
import groupquattro.demo.mapper.CKExpenceMapperImpl;
import groupquattro.demo.model.CKExpence;
import groupquattro.demo.model.CKExpenceBuilder;
import groupquattro.demo.model.Debt;
import groupquattro.demo.repos.CKExpenceRepository;
import groupquattro.demo.utils.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CKExpenceServiceImpl implements CKExpenceService {
    @Autowired
    private ChestServiceImpl chestService;
    @Autowired
    private CKExpenceRepository expenceRepository;

    @Autowired
    private GroupServiceImpl groupService;

    @Autowired
    private DebtServiceImpl debtService;

    private CKExpenceMapper expenceMapper = new CKExpenceMapperImpl();

    private CKExpenceFormMapper formMapper = new CKExpenceFormMapperImpl();


    @Override
    public CKExpenceSummaryDto findCKExpenceByIdExpence(String idExpence) throws ResourceNotFoundException {

        Optional<CKExpence> e = expenceRepository.findById(idExpence);
        if(!e.isPresent()){
            throw new ResourceNotFoundException("expence "+idExpence+ " not found.");
        }
        else{
            return expenceMapper.toDto(e.get());
        }
    }

    @Override
    public List<CKExpenceSummaryDto> getAllCKExpences() throws ResourceNotFoundException {
        List<CKExpence> expencesList = expenceRepository.findAll();
        if(expencesList.isEmpty()){
            throw new ResourceNotFoundException("expence list not found");
        }
        else{
            return expenceMapper.toListCKExpenceSummaryDto(expencesList);
        }
    }

    @Override
    public CKExpenceSummaryDto withdraw(String expenceId, String username) throws UserNotOwnerException, ChestNotOpenedException, ResourceNotFoundException, UserUpdateException {
        Optional<CKExpence> optionalCKExpence = expenceRepository.findById(expenceId);
        if(optionalCKExpence.isPresent()){
            CKExpence e = optionalCKExpence.get();
            double value = e.getChest().getChestKey().getListOfOwners().get(username);//value owned by user
            chestService.withdraw(e.getChest().getId(), username);//update chest and owners list
            double costPaidByThisMember = e.getPayingMembers().get(username);
            costPaidByThisMember -= value;
            e.getPayingMembers().put(username, Round.round(costPaidByThisMember, 2));//update payingMembersMap
            expenceRepository.save(e);
            return expenceMapper.toDto(expenceRepository.findById(e.getId()).get());
        }
        else{
            throw new ResourceNotFoundException("expence "+ expenceId + " not found");
        }
    }

    @Override
    public CKExpenceSummaryDto deposit(String expenceId, String username) throws ChestOpenedException, UserNotDebtorException, ResourceNotFoundException, UserUpdateException {
        Optional<CKExpence> optionalCKExpence = expenceRepository.findCKExpenceById(expenceId);
        if(optionalCKExpence.isPresent()){
            CKExpence e = optionalCKExpence.get();
            double value = 0.00;
            Debt debt = e.getUserDebt(username);
            value = debt.getDebt(); //used to update the payingMembers Map as well
            chestService.deposit(e.getChest().getId(), value); //chest update
            debtService.deleteUserDebt(debt, username);//debt and user (debtor) both get updated
            e.getDebts().remove(debt);
            double costPaidByThisMember = e.getPayingMembers().get(username);
            costPaidByThisMember -= value;
            e.getPayingMembers().put(username, Round.round(costPaidByThisMember, 2));
            e = expenceRepository.save(e);
            return expenceMapper.toDto(expenceRepository.findById(e.getId()).get());
        }
        else{
            throw new ResourceNotFoundException("expence "+ expenceId + " not found");
        }
    }


    @Override
    public CKExpenceSummaryDto createCKExpence(CKExpenceFormDto formDto) throws ResourceNotFoundException
    {
        try {
            CKExpence formExpence = formMapper.toModel(formDto);
            CKExpenceBuilder builder = null;
            CKExpence e = null;
            try {
                builder = CKExpence.builder.reset(formExpence);
                builder.chest(formExpence.getCost(), formExpence.getPayingMembers());
            }catch (SumIsNotCorrectException exception){
                e = builder.build();
                e.setChest(chestService.createChest(e.getChest())); //make the chest persistent
                e.setDebts(debtService.createDebts(e.getDebts()));//make the debts persistent
                CKExpence savedEntity = expenceRepository.save(e);//make this expence persistent
                CKExpenceSummaryDto ckExpenceSummaryDto = expenceMapper.toDto(savedEntity);
                //add field to dto that specifies that total cost is not reached by payingMembers payings
                //this is shown only the first time the expence "receipt" is returned
                //each time the user retrieves this expence the message won't be included
                ckExpenceSummaryDto.setMessage(exception.getLocalizedMessage());
                addExpenceToGroup(savedEntity, formDto.getGroupName()); //update group's expenceList

                return ckExpenceSummaryDto;
            }
            e = builder.build();
            e.setChest(chestService.createChest(e.getChest())); //make the chest persistent
            e.setDebts(debtService.createDebts(e.getDebts()));//make the debts persistent
            CKExpence savedEntity = expenceRepository.save(e);//make this expence persistent
            CKExpenceSummaryDto ckExpenceSummaryDto = expenceMapper.toDto(savedEntity);
            addExpenceToGroup(savedEntity, formDto.getGroupName()); //update group's expenceList
            return ckExpenceSummaryDto;
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public void delete(String expenceId) throws ResourceNotFoundException {
        try{
            CKExpence target = expenceRepository.findCKExpenceById(expenceId).orElseThrow();
        }catch (Exception e){
            throw new ResourceNotFoundException("the expence was not found");
        }
        expenceRepository.deleteById(expenceId);
    }

    private void addExpenceToGroup(CKExpence ckExpence, String groupName) throws ResourceNotFoundException {
        groupService.updateGroupExpences(ckExpence, groupName);
    }

}
