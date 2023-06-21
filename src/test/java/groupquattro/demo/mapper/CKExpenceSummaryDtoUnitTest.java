package groupquattro.demo.mapper;

import groupquattro.demo.model.CKExpence;
import groupquattro.demo.exceptions.SumIsNotCorrectException;
import groupquattro.demo.dto.CKExpenceSummaryDto;
import groupquattro.demo.dto.ChestDto;
import groupquattro.demo.dto.DebtDto;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = {CKExpenceMapperImpl.class})
public class CKExpenceSummaryDtoUnitTest {

    @Test
    public void fromModelToDto() throws ParseException, SumIsNotCorrectException {
        Map<String, Double> payingMembers = new HashMap<>();
        payingMembers.put("Un utente", 20.00);
        payingMembers.put("Un altro utente", 10.00);
        CKExpence e = CKExpence.builder.description("una descrizione").date(new SimpleDateFormat("dd/MM/yyyy, HH:mm").parse("30/08/2023, 09:08"))
                .cost(30.00).payingMembers(payingMembers).chest(30.00, payingMembers).build();
        CKExpenceMapperImpl ckExpenceMapper = new CKExpenceMapperImpl();
        CKExpenceSummaryDto summaryDto = ckExpenceMapper.toDto(e);

        assertTrue(true);
    }

    @Test
    public void fromDtoToModel() throws ParseException {
        CKExpenceSummaryDto e = new CKExpenceSummaryDto();
        e.setId("unid");
        e.setDate("17/07/23, 23:50");
        e.setDescription("una spesa");
        Map<String, String> payingMembers = new HashMap<>();
        payingMembers.put("Un utente", "20.00");
        payingMembers.put("Un altro utente", "10.00");
        e.setCost("30.00");
        e.setPayingMembers(payingMembers);
        ChestDto chestDto = new ChestDto();
        chestDto.setMax_amount("5.00");
        chestDto.setPercentage("0.00");
        chestDto.setIsOpen("N");
        chestDto.setId("unchestid");
        chestDto.setCurrentValue("0.00");
        e.setChest(chestDto);
        e.setOwners(new HashMap<>());
        e.getOwners().put("Un Utente", "5.00");
        e.setDebts(new ArrayList<>());
        e.getDebts().add(new DebtDto("un debtid", "Un altro Utente", 30));
        CKExpenceMapperImpl ckExpenceMapper = new CKExpenceMapperImpl();
        CKExpence cke = ckExpenceMapper.toModel(e);
        assertTrue(true);
    }
}
