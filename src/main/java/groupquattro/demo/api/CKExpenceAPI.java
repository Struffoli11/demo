package groupquattro.demo.api;

import groupquattro.demo.dto.CKExpenceFormDto;
import groupquattro.demo.dto.CKExpenceSummaryDto;
import groupquattro.demo.dto.ServerResponse;
import groupquattro.demo.exceptions.*;
import groupquattro.demo.services.CKExpenceService;
import groupquattro.demo.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expences")
public class CKExpenceAPI {

    @Autowired
    private GroupService groupService;
    @Autowired
    private CKExpenceService expenceService;

//    @GetMapping
//    public ResponseEntity<?> getAllExpences() throws ResourceNotFoundException {
//        List<CKExpenceSummaryDto> expenceSummaryDtoList = expenceService.getAllCKExpences();
//        return ResponseEntity.ok(expenceSummaryDtoList);
//    }

    @GetMapping("/{expenceId}")
    public ResponseEntity<?> getExpenceById(@PathVariable String expenceId) throws ResourceNotFoundException {
        CKExpenceSummaryDto expenceSummaryDto = expenceService.findCKExpenceByIdExpence(expenceId);
        return ResponseEntity.ok(expenceSummaryDto);
    }

    @PostMapping
    public ResponseEntity<?> createExpence(@RequestBody CKExpenceFormDto formDto){

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            String groupName = formDto.getGroupName();
            CKExpenceSummaryDto receipt = null;
            if (groupService.getGroupMembers(groupName).contains(username)) {
                receipt = expenceService.createCKExpence(formDto);

                URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{expenceId}")
                        .buildAndExpand(receipt.getId())
                        .toUri();
                return ResponseEntity.created(uri).body(receipt);
            } else
                return ResponseEntity.status(401).body(new ServerResponse("User is not member of group " + groupName));
        }catch(Exception e){
            return ResponseEntity.status(404).body(new ServerResponse(e.getLocalizedMessage()));
        }
    }

    @GetMapping("/{expenceId}/withdraw/")
    public ResponseEntity<?> withdrawFromChest(@PathVariable("expenceId") String expenceId) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            CKExpenceSummaryDto expenceSummaryDto = expenceService.withdraw(expenceId, username);
            return ResponseEntity.ok(expenceSummaryDto);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(404).body(new ServerResponse(e.getLocalizedMessage()));
        }catch(UserNotOwnerException | ChestNotOpenedException | UserUpdateException e2){
            return ResponseEntity.status(400).body(new ServerResponse(e2.getLocalizedMessage()));
        }
    }


    @PostMapping("/{expenceId}/payment/")
    public ResponseEntity<?> depositChest(@PathVariable String expenceId) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            CKExpenceSummaryDto expenceSummaryDto = expenceService.deposit(expenceId, username);
            return ResponseEntity.ok(expenceSummaryDto);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(404).body(new ServerResponse(e.getLocalizedMessage()));
        }catch(UserNotDebtorException | ChestOpenedException | UserUpdateException e2){
            return ResponseEntity.status(400).body(new ServerResponse(e2.getLocalizedMessage()));
        }
    }

    @DeleteMapping("/{expenceId}")
    public ResponseEntity<?> deleteExpence(@PathVariable String expenceId) throws ResourceNotFoundException{
        try{
            expenceService.delete(expenceId);
            return ResponseEntity.status(204).body("The expence was deleted");
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.status(404).body(new ServerResponse("the expence could not be deleted"));
        }
    }
}

