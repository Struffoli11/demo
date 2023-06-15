package groupquattro.demo.services;

import groupquattro.demo.dto.DebtDto;
import groupquattro.demo.exceptions.DuplicateResourceException;
import groupquattro.demo.exceptions.ResourceNotFoundException;
import groupquattro.demo.model.Debt;

import java.util.List;

public interface DebtService {

    public DebtDto findDebtById(String id) throws ResourceNotFoundException;

    public List<DebtDto> getAllDebts() throws ResourceNotFoundException;

    public DebtDto createDebt(DebtDto debt) throws DuplicateResourceException;

    public DebtDto createDebt(Debt debt);

    void deleteUserDebt(Debt debt, String username) throws ResourceNotFoundException;

    List<Debt> createDebts(List<Debt> debts) throws ResourceNotFoundException;
}
