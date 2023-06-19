package groupquattro.demo.mapper;

import groupquattro.demo.dto.CKExpenceSummaryDto;
import groupquattro.demo.model.CKExpence;
import groupquattro.demo.model.Chest;
import org.mapstruct.*;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "default", uses = {DebtMapper.class, ChestMapper.class, UtilityMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
public interface CKExpenceMapper {

    @Mapping(target = "owners", source = "chest", qualifiedByName = "ownersFormat")
    public CKExpenceSummaryDto toDto(CKExpence e);

    @Mapping(target = "cost", source = "cost")
    @Mapping(target = "date", source = "date", dateFormat = "dd/MM/yyyy, HH:mm")
    @Mapping(target = "payingMembers", source = "payingMembers")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "debts", source = "debts")
    public CKExpence toModel(CKExpenceSummaryDto eSum);

    public List<CKExpence> toListCKExpence(List<CKExpenceSummaryDto> listDto);

    public List<CKExpenceSummaryDto> toListCKExpenceSummaryDto(List<CKExpence> listDto);

    @Named("ownersFormat")
    default  Map<String , String>  formatOwners(Chest ch) {
        if(ch != null){
            UtilityMapperImpl utilityMapper = new UtilityMapperImpl();
            return utilityMapper.getMap(ch.getChestKey().getListOfOwners());
        }else{
            return null;
        }
    }
}
