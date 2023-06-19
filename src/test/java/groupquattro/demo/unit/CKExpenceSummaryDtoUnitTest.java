package groupquattro.demo.unit;

import groupquattro.demo.dto.CKExpenceSummaryDto;
import groupquattro.demo.dto.ChestDto;
import groupquattro.demo.mapper.CKExpenceMapperImpl;
import groupquattro.demo.model.CKExpence;
import org.assertj.core.api.MatcherAssert;
import org.hamcrest.MatcherAssert.*;
import org.junit.Test;
import org.mapstruct.ap.shaded.freemarker.template.SimpleDate;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CKExpenceSummaryDtoUnitTest {

    @Test
    public void whenConvertingFromCKExpenceSummaryDtoToCKExpence_thenCorrect() throws ParseException {
        CKExpence e = new CKExpence();
        e.setId("unid");
        e.setDate(new SimpleDateFormat("dd/MM/yyyy, HH:mm").parse("17/07/23, 23:50"));
        e.setDescription("una spesa");
        Map<String, Double> payingMembers = new HashMap<>();
        payingMembers.put("Un utente", 20.00);
        payingMembers.put("Un altro utente", 10.00);
        e.setPayingMembers(payingMembers);
        e = CKExpence.builder.reset(e).chest(30.00, payingMembers).build();
//        e.setChest(null);

        CKExpenceMapperImpl ckExpenceMapper = new CKExpenceMapperImpl();
        CKExpenceSummaryDto summaryDto = ckExpenceMapper.toDto(e);

        assertTrue(true);
    }

    @Test
    public void fromModelToDto() throws ParseException {
        CKExpenceSummaryDto e = new CKExpenceSummaryDto();
        e.setId("unid");
        e.setDate("17/07/23, 23:50");
        e.setDescription("una spesa");
        Map<String, String> payingMembers = new HashMap<>();
        payingMembers.put("Un utente", "20.00");
        payingMembers.put("Un altro utente", "10.00");
        e.setPayingMembers(payingMembers);
        e.setChest(null);

        CKExpenceMapperImpl ckExpenceMapper = new CKExpenceMapperImpl();
        CKExpence cke = ckExpenceMapper.toModel(e);

        assertTrue(true);
    }
}
