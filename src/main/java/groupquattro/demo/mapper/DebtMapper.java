package groupquattro.demo.mapper;

import groupquattro.demo.dto.DebtDto;
import groupquattro.demo.model.Debt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface DebtMapper {


    public Debt toModel(DebtDto debtDto);

    public DebtDto toDto(Debt debt);

    List<DebtDto> convertDebtList(List<Debt> debtList);
}
