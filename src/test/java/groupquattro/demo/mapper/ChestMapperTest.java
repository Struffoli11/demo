package groupquattro.demo.mapper;

import groupquattro.demo.model.Chest;
import groupquattro.demo.dto.ChestDto;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest(classes = {ChestMapperTest.class})
public class ChestMapperTest {

    @Test
    public void fromModelToDto(){
        Chest chest = new Chest();
        chest.setOpen(true);
        chest.setCurrentValue(50.00);
        chest.setMax_amount(200.00);

        ChestMapperImpl mapper = new ChestMapperImpl();
        ChestDto chestDto = mapper.toDto(chest);

        assertThat(Double.valueOf(chestDto.getPercentage()), is(chest.computePercentage()));
        assertThat(chestDto.getIsOpen(), is("Y"));
        assertThat(Double.valueOf(chestDto.getMax_amount()), is(chest.getMax_amount()));
    }

    @Test
    public void fromDtoToModel(){
        ChestDto chestDto = new ChestDto();
        chestDto.setId("id");
        chestDto.setPercentage("50.00");
        chestDto.setCurrentValue("50.000");
        chestDto.setMax_amount("100.00");
        chestDto.setIsOpen("Y");

        ChestMapperImpl chestMapper = new ChestMapperImpl();
        Chest aChest = chestMapper.toModel(chestDto);

        MatcherAssert.assertThat(aChest.isOpen(), is(true));
    }

}
