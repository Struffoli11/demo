package groupquattro.demo.mapper;

import groupquattro.demo.dto.ChestDto;
import groupquattro.demo.model.Chest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "default", uses = {BooleanYNMapper.class})
public interface ChestMapper {

    @Mapping(target = "chestKey", ignore = true)
    @Mapping(target = "open", source = "isOpen")
    public Chest toModel(ChestDto chestDto);

    @Mapping(target = "percentage", expression = "java(String.valueOf(chest.computePercentage()))")
    @Mapping(target = "isOpen", source = "open")
    public ChestDto toDto(Chest chest);

    public List<ChestDto> convertChestListToDtoList(List<Chest> chestList);
}
