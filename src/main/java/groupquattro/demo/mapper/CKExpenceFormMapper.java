package groupquattro.demo.mapper;

import groupquattro.demo.dto.CKExpenceFormDto;
import groupquattro.demo.model.CKExpence;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {UtilityMapper.class})
public interface CKExpenceFormMapper {

    @Mapping(target = "chest", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "debts", ignore = true)
    @Mapping(target = "date", source = "date", dateFormat = "dd/MM/yyyy, HH:mm")
    @Mapping(target = "cost", source = "cost")
    @Mapping(target = "payingMembers", source = "payingMembers")
    public CKExpence toModel(CKExpenceFormDto dto);
}
