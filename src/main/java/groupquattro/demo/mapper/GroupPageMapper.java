package groupquattro.demo.mapper;

import groupquattro.demo.dto.GroupPageDto;
import groupquattro.demo.model.Group;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "default", uses = CKExpenceMapper.class)
public interface GroupPageMapper {

    public Group toModel(GroupPageDto groupPageDto);

    @InheritInverseConfiguration(name = "toModel")
    public GroupPageDto toDto(Group group);

    public List<GroupPageDto> convertGroupListToGroupDtoList(List<Group> groupList);

    public Group updateModel(GroupPageDto groupPageDto, @MappingTarget Group group);
}
