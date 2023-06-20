package groupquattro.demo.mapper;

import groupquattro.demo.dto.UserDto;
import groupquattro.demo.model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "default", uses = {GroupPageMapper.class, DebtMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    public User toModel(UserDto userDto);

    @InheritInverseConfiguration(name = "toModel")
    public UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    public User updateModel(UserDto userDto, @MappingTarget User user);

    public List<User> convertUserDtoListToUserList(List<UserDto> users);

    public List<UserDto> convertUserListToUserDtoList(List<User> users);


}

