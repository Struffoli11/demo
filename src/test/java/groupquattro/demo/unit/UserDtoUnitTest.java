package groupquattro.demo.unit;

import groupquattro.demo.dto.CKExpenceSummaryDto;
import groupquattro.demo.dto.DebtDto;
import groupquattro.demo.dto.GroupPageDto;
import groupquattro.demo.dto.UserDto;
import groupquattro.demo.mapper.UserDtoMapperImpl;
import groupquattro.demo.model.User;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
@SpringBootTest(classes = {UserDtoMapperImpl.class})
public class UserDtoUnitTest {

    private final UserDtoMapperImpl userDtoMapper = new UserDtoMapperImpl();

    @Test
    public void whenConvertingFromUserDtoToUser_thenCorrect(){
        UserDto userDto = new UserDto();
        userDto.setUsername("Anton");
        userDto.setEmail("anton@email.com");
        userDto.setGroups(new ArrayList<GroupPageDto>());
        userDto.setDebts(new ArrayList<DebtDto>());
        GroupPageDto groupPageDto = new GroupPageDto();
        groupPageDto.setGroupName("Estate");
        groupPageDto.setIdGroup("id1");
        groupPageDto.setGroupOwner("Anton");
        groupPageDto.setMembers(new ArrayList<String>());
        groupPageDto.getMembers().add("Anton");
        groupPageDto.setExpences(new ArrayList<CKExpenceSummaryDto>());
        userDto.getGroups().add(groupPageDto);
        User aUser = userDtoMapper.toModel(userDto);
        assertThat(aUser.getUsername(), is(userDto.getUsername()));
    }
}
