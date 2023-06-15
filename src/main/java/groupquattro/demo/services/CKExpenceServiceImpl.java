package groupquattro.demo.services;

import groupquattro.demo.dto.CKExpenceFormDto;
import groupquattro.demo.dto.CKExpenceSummaryDto;
import groupquattro.demo.exceptions.*;
import groupquattro.demo.mapper.CKExpenceFormMapper;
import groupquattro.demo.mapper.CKExpenceMapper;
import groupquattro.demo.model.CKExpence;
import groupquattro.demo.model.Debt;
import groupquattro.demo.repos.CKExpenceRepository;
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

    @Autowired
    private CKExpenceMapper expenceMapper;

    @Autowired
    private CKExpenceFormMapper formMapper;


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
            chestService.withdraw(e.getChest().getId(), username);
            //not needed to update the @code expence, since it refers to the chest through the chest id
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
            value = debt.getDebt();
            chestService.deposit(e.getChest().getId(), value); //chest update
            debtService.deleteUserDebt(debt, username);//debt and user (debtor) both get updated
            return expenceMapper.toDto(expenceRepository.findById(e.getId()).get());
        }
        else{
            throw new ResourceNotFoundException("expence "+ expenceId + " not found");
        }
    }


    @Override
    public CKExpenceSummaryDto createCKExpence(CKExpenceFormDto formDto) throws ResourceNotFoundException
    {
        CKExpence formExpence = formMapper.toModel(formDto);
        CKExpence e = CKExpence.builder.reset(formExpence)
                .chest(formExpence.getCost(), formExpence.getPayingMembers())
                .build();
        e.setChest(chestService.createChest(e.getChest())); //make the chest persistent
        e.setDebts(debtService.createDebts(e.getDebts()));//make the debts persistent
        CKExpence savedEntity = expenceRepository.save(e);//make this expence persistent
        CKExpenceSummaryDto ckExpenceSummaryDto = expenceMapper.toDto(savedEntity);
        addExpenceToGroup(ckExpenceSummaryDto, formDto.getGroupName()); //update group's expenceList
        return ckExpenceSummaryDto;
    }

    private void addExpenceToGroup(CKExpenceSummaryDto ckExpenceSummaryDto, String groupName) throws ResourceNotFoundException {
        groupService.updateGroupExpences(ckExpenceSummaryDto, groupName);
    }

}
