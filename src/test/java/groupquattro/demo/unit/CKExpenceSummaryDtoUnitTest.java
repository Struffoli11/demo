package groupquattro.demo.unit;

import groupquattro.demo.dto.CKExpenceSummaryDto;
import groupquattro.demo.dto.ChestDto;
import groupquattro.demo.mapper.CKExpenceMapperImpl;
import groupquattro.demo.model.CKExpence;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedHashMap;

public class CKExpenceSummaryDtoUnitTest {

    @Test
    public void whenConvertingFromCKExpenceSummaryDtoToCKExpence_thenCorrect(){
        CKExpenceSummaryDto ckExpenceSummaryDto = new CKExpenceSummaryDto();
        ckExpenceSummaryDto.setId("id2");
        ckExpenceSummaryDto.setDate("24-05-2023");
        ckExpenceSummaryDto.setCost("23.00");
        ckExpenceSummaryDto.setPayingMembers(new LinkedHashMap<String, String>());
        ckExpenceSummaryDto.setChest(new ChestDto("idChest", "0.00"));

        CKExpence e = new CKExpenceMapperImpl().toModel(ckExpenceSummaryDto);
        assertTrue(true);
    }
}
