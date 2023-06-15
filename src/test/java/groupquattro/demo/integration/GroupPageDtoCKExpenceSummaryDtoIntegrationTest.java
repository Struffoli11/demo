package groupquattro.demo.integration;

import groupquattro.demo.dto.GroupPageDto;
import groupquattro.demo.mapper.GroupPageMapperImpl;
import groupquattro.demo.model.CKExpence;
import groupquattro.demo.model.Group;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class GroupPageDtoCKExpenceSummaryDtoIntegrationTest {

    @Test
    public void whenConvertGroupEntityToGroupDto_thenCorrect() throws ParseException {
        Group aGroup = new Group();
        aGroup.setIdGroup("id");
        aGroup.setGroupName("A Group");
        aGroup.setGroupOwner("Antonio");
        aGroup.setMembers(new ArrayList<String>());
        aGroup.getMembers().add("Antonio");
        aGroup.getMembers().add("Marco");
        List<CKExpence> expences = new ArrayList<CKExpence>();
        Map<String, Double> pMembers = new LinkedHashMap<String, Double>();
        pMembers.put("Antonio", 20.00);
        pMembers.put("Marco", 3.00);

        CKExpence cke = CKExpence.builder.reset()
                        .date(new SimpleDateFormat("dd-MM-yyyy").parse("24-05-2023"))
                                        .description("una descrizione")
                                                .cost(23.00)
                                                        .payingMembers(pMembers)
                                                                .chest(23.00, pMembers)
                                                                        .build();
        cke.setId("unid");
        expences.add(cke);
        aGroup.setExpences(expences);

        GroupPageDto groupPageDto = new GroupPageMapperImpl().toDto(aGroup);
        assertEquals(aGroup.getIdGroup(), groupPageDto.getIdGroup());
        assertEquals(aGroup.getGroupName(), groupPageDto.getGroupName());
        assertEquals(aGroup.getMembers(), groupPageDto.getMembers().stream().map(temp->{
            String memberUsername = temp;
            return memberUsername;
        }).collect(Collectors.toList()));

        assertEquals(aGroup.getGroupOwner(), groupPageDto.getGroupOwner());
    }
}
