package groupquattro.demo.mapper;

import groupquattro.demo.dto.UserDto;
import groupquattro.demo.dto.UserInfoDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserInfoMapper {

    public UserInfoDto toInfo(UserDto userDto);
    public List<UserInfoDto> toListUserInfo(List<UserDto> userDto);
}
