package groupquattro.demo.unit;

import groupquattro.demo.dto.CKExpenceSummaryDto;
import groupquattro.demo.dto.DebtDto;
import groupquattro.demo.dto.GroupPageDto;
import groupquattro.demo.dto.UserDto;
import groupquattro.demo.mapper.UserDtoMapper;
import groupquattro.demo.mapper.UserDtoMapperImpl;
import groupquattro.demo.model.Debt;
import groupquattro.demo.model.User;
import org.junit.Test;

import java.util.ArrayList;

public class UserDtoUnitTest {

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
        User user = new UserDtoMapperImpl().toModel(userDto);
    }
}
