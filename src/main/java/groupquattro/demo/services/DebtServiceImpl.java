package groupquattro.demo.services;

import groupquattro.demo.dto.DebtDto;
import groupquattro.demo.dto.UserDto;
import groupquattro.demo.exceptions.DuplicateResourceException;
import groupquattro.demo.exceptions.ResourceNotFoundException;
import groupquattro.demo.mapper.DebtMapperImpl;
import groupquattro.demo.mapper.UserDtoMapper;
import groupquattro.demo.model.Debt;
import groupquattro.demo.repos.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class DebtServiceImpl implements DebtService{

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private UserService userService;

    private DebtMapperImpl debtMapper = new DebtMapperImpl();
    @Override
    public DebtDto findDebtById(String id) throws ResourceNotFoundException {
        Optional<Debt> optionalDebt = debtRepository.findDebtById(id);
        if(optionalDebt.isPresent()){
            return debtMapper.toDto(optionalDebt.get());
        }
        else{
            throw new ResourceNotFoundException("debt with id:" + id + " not found");
        }
    }

    @Override
    public List<DebtDto> getAllDebts() throws ResourceNotFoundException {
        List<Debt> debtList = debtRepository.findAll();
        if(debtList.isEmpty()){
            throw new ResourceNotFoundException("debtList not found");
        }
        else{
            return debtMapper.convertDebtList(debtList);
        }
    }

    @Override
    public DebtDto createDebt(DebtDto debtDto) throws DuplicateResourceException {
        Debt debt = debtMapper.toModel(debtDto);
        Debt savedDebt = debtRepository.save(debt);
        return debtMapper.toDto(savedDebt);
    }

    @Override
    public DebtDto createDebt(Debt debt) {
        Debt savedDebt = debtRepository.save(debt);
        return debtMapper.toDto(savedDebt);
    }

    @Override
    public void deleteUserDebt(Debt debt, String username) throws ResourceNotFoundException {
        if(debtRepository.findById(debt.getId()).isPresent()) {
            DebtDto debtDto = debtMapper.toDto(debt);
            debtRepository.delete(debt);
            userService.deleteUserDebt(debtDto, username);
        }
        else{
            throw new ResourceNotFoundException("no debt found");
        }
    }

    @Override
    public List<Debt> createDebts(List<Debt> debts) throws ResourceNotFoundException {
        List<Debt> savedList = debtRepository.saveAll(debts);
        UserDto debtor = null;
        String debtorUsername = "";
        for(Debt debt : savedList){
            debtorUsername = debt.getDebtor();
            userService.createDebt(debt, debtorUsername);
        }
        return savedList;
    }
}
