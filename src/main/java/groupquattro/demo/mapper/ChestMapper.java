package groupquattro.demo.mapper;

import groupquattro.demo.dto.ChestDto;
import groupquattro.demo.model.Chest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ChestMapper {
    @Mapping(target = "id", defaultValue = "id")
    @Mapping(target = "max_amount", ignore = true)
    @Mapping(target = "open", ignore = true)
    @Mapping(target = "chestKey", ignore = true)
    public Chest toModel(ChestDto chestDto);

    @Mapping(target = "id", defaultValue = "id")
    public ChestDto toDto(Chest chest);

    public List<ChestDto> convertChestListToDtoList(List<Chest> chestList);
}
