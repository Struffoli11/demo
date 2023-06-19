package groupquattro.demo.services;

import groupquattro.demo.dto.CKExpenceFormDto;
import groupquattro.demo.dto.CKExpenceSummaryDto;
import groupquattro.demo.exceptions.*;
import groupquattro.demo.model.CKExpence;
import groupquattro.demo.model.Chest;

import java.util.List;
import java.util.Optional;

public interface CKExpenceService {


    CKExpenceSummaryDto findCKExpenceByIdExpence(String idExpence) throws ResourceNotFoundException;

    List<CKExpenceSummaryDto> getAllCKExpences() throws ResourceNotFoundException;

    CKExpenceSummaryDto withdraw(String expenceId, String username) throws UserNotOwnerException, ChestNotOpenedException, ResourceNotFoundException, UserUpdateException;

    CKExpenceSummaryDto deposit(String expenceId, String username) throws ChestOpenedException, UserNotDebtorException, ResourceNotFoundException, UserUpdateException;

    CKExpenceSummaryDto createCKExpence(CKExpenceFormDto e) throws ResourceNotFoundException;

}
