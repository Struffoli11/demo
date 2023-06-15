package groupquattro.demo.mapper;

import groupquattro.demo.dto.RegisterRequestDto;
import groupquattro.demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserRegistrationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "debts", ignore = true)
    @Mapping(target = "keys", ignore = true)
    @Mapping(target = "role", ignore = true)
    public User toModel(RegisterRequestDto userRegistrationDto);

}
