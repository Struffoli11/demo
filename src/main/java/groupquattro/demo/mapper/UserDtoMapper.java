package groupquattro.demo.mapper;

import groupquattro.demo.dto.UserDto;
import groupquattro.demo.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(uses = {GroupPageMapperImpl.class, DebtMapperImpl.class})
public interface UserDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    public User toModel(UserDto userDto);

    @InheritInverseConfiguration(name = "toModel")
    public UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    public User updateModel(UserDto userDto, @MappingTarget User user);

    public List<User> convertUserDtoListToUserList(List<UserDto> users);

    public List<UserDto> convertUserListToUserDtoList(List<User> users);


}

