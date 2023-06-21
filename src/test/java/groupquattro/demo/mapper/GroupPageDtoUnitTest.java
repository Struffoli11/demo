package groupquattro.demo.mapper;


import groupquattro.demo.model.CKExpence;
import groupquattro.demo.model.Group;
import groupquattro.demo.model.User;
import groupquattro.demo.dto.GroupPageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = {GroupPageMapperImpl.class})
public class GroupPageDtoUnitTest {

    @BeforeEach
    public void init(){
        User aUser = new User("Antonio", "antonio");
        Group aGroup = new Group("AGroup", "Antonio");
    }

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
