package groupquattro.demo.api;

import groupquattro.demo.dto.CKExpenceFormDto;
import groupquattro.demo.dto.CKExpenceSummaryDto;
import groupquattro.demo.exceptions.*;
import groupquattro.demo.services.CKExpenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expences")
public class CKExpenceAPI {

    @Autowired
    private CKExpenceService expenceService;

    @GetMapping
    public ResponseEntity<?> getAllExpences() throws ResourceNotFoundException {
        List<CKExpenceSummaryDto> expenceSummaryDtoList = expenceService.getAllCKExpences();
        return ResponseEntity.ok(expenceSummaryDtoList);
    }

    @GetMapping("/{expenceId}")
    public ResponseEntity<?> getExpenceById(@PathVariable String idExpence) throws ResourceNotFoundException {
        CKExpenceSummaryDto expenceSummaryDto = expenceService.findCKExpenceByIdExpence(idExpence);
        return ResponseEntity.ok(expenceSummaryDto);
    }

    @PostMapping
    public ResponseEntity<?> createExpence(@RequestBody CKExpenceFormDto formDto) throws ParseException, WrongExpenceTypeException, ResourceNotFoundException {
        CKExpenceSummaryDto receipt = expenceService.createCKExpence(formDto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{expenceId}")
                .buildAndExpand(receipt.getId())
                .toUri();
        return ResponseEntity.created(uri).body(receipt);
    }

    @GetMapping("/{expenceId}/withdraw/")
    public ResponseEntity<?> withdrawFromChest(@PathVariable("expenceId") String expenceId, @RequestBody String username) throws ChestNotOpenedException, UserNotOwnerException, WrongExpenceTypeException, ResourceNotFoundException, UserUpdateException {
        CKExpenceSummaryDto expenceSummaryDto = expenceService.withdraw(expenceId, username);
        return ResponseEntity.ok(expenceSummaryDto);
    }


    @PostMapping("/{expenceId}/payment/")
    public ResponseEntity<?> depositChest(@PathVariable String expenceId, @RequestBody String username) throws UserNotDebtorException, ChestOpenedException, WrongExpenceTypeException, ResourceNotFoundException, UserUpdateException {
        CKExpenceSummaryDto expenceSummaryDto = expenceService.deposit(expenceId, username);
        return ResponseEntity.ok(expenceSummaryDto);
    }
}

