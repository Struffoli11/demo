package groupquattro.demo.mapper;

import groupquattro.demo.dto.CKExpenceSummaryDto;
import groupquattro.demo.model.CKExpence;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {ChestMapper.class, UtilityMapper.class})
public interface CKExpenceMapper {


    public CKExpenceSummaryDto toDto(CKExpence e);

    @Mapping(target = "cost", source = "cost")
    @Mapping(target = "date", source = "date", dateFormat = "dd/MM/yyyy, HH:mm")
    @Mapping(target = "payingMembers", source = "payingMembers")
    @Mapping(target = "debts", ignore = true)
    @Mapping(target = "id", ignore = true)
    public CKExpence toModel(CKExpenceSummaryDto eSum);

    public List<CKExpence> toListCKExpence(List<CKExpenceSummaryDto> listDto);

    public List<CKExpenceSummaryDto> toListCKExpenceSummaryDto(List<CKExpence> listDto);
}
