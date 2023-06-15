package groupquattro.demo.unit;

import groupquattro.demo.dto.CKExpenceFormDto;
import groupquattro.demo.mapper.CKExpenceFormMapper;
import groupquattro.demo.mapper.CKExpenceFormMapperImpl;
import groupquattro.demo.model.CKExpence;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

public class CKExpenceFormUnitTest {

    @Test
    public void whenConvertingFromCKExpenceFormDtoToModel_thenCorrect(){
        CKExpenceFormDto formDTO = new CKExpenceFormDto();
        formDTO.setDescription("una descrizione");
        formDTO.setDate("24-05-2023");
        formDTO.setGroupName("My Group");
        formDTO.setCost("24.90");
        Map<String, String> pMembers = new LinkedHashMap<String, String>();
        pMembers.put("Antonio", "13.90");
        pMembers.put("Francesco", "7.00");
        pMembers.put("Luca", "4.00");
        formDTO.setPayingMembers(pMembers);

        CKExpence cke = new CKExpenceFormMapperImpl().toModel(formDTO);

        assertTrue(true);
    }
}
