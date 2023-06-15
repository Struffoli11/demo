package groupquattro.demo.unit;


import groupquattro.demo.dto.GroupPageDto;
import groupquattro.demo.mapper.GroupPageMapperImpl;
import groupquattro.demo.model.CKExpence;
import groupquattro.demo.model.Group;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GroupPageDtoUnitTest {
//
//    @Before
//    public void init(){
//        User aUser = new User("Antonio", "antonio");
//        Group aGroup = new Group("AGroup", "Antonio");
//    }

    @Test
    public void whenConvertGroupEntityToGroupDto_thenCorrect(){
        Group aGroup = new Group();
        aGroup.setIdGroup("id");
        aGroup.setGroupName("A Group");
        aGroup.setGroupOwner("Antonio");
        aGroup.setMembers(new ArrayList<String>());
        aGroup.getMembers().add("Antonio");
        aGroup.setExpences(new ArrayList<CKExpence>());

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
