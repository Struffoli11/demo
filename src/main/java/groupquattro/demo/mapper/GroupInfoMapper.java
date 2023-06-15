package groupquattro.demo.mapper;

import groupquattro.demo.dto.GroupInfoDto;
import groupquattro.demo.dto.GroupPageDto;
import groupquattro.demo.model.Group;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface GroupInfoMapper {

    public GroupInfoDto fromGroupPageDtoToGroupInfoDto(GroupPageDto groupPageDto);

    public GroupInfoDto fromGroupToGroupInfoDto(Group group);

    public List<GroupInfoDto> convertGroupListToGroupInfoList(List<Group> groupList);

    public List<GroupInfoDto> convertGroupPageDtoListToGroupInfoList(List<GroupPageDto> groupList);

}
