package groupquattro.demo.mapper;

import groupquattro.demo.model.Debt;
import groupquattro.demo.dto.DebtDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = {DebtMapperImpl.class})
public class DebtDtoUnitTest {

    private final DebtMapperImpl debtMapper = new DebtMapperImpl();
    @Test
    public void whenConvertingDebtEntityToDebt_thenCorrect(){
        Debt debt = new Debt("id", "Antonio", "idExpence", 20.00);
        DebtDto debtDto = debtMapper.toDto(debt);
        assertTrue(debtDto.getDebtor()!=null);
    }
}
