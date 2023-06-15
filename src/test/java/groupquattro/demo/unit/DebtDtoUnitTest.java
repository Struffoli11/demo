package groupquattro.demo.unit;

import groupquattro.demo.dto.DebtDto;
import groupquattro.demo.mapper.DebtMapper;
import groupquattro.demo.mapper.DebtMapperImpl;
import groupquattro.demo.model.Debt;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DebtDtoUnitTest {

    @Autowired(required = true)
    private DebtMapperImpl debtMapper;
    @Test
    public void whenConvertingDebtEntityToDebt_thenCorrect(){
        Debt debt = new Debt("id", "Antonio", "idExpence", 20.00);
        DebtDto debtDto = debtMapper.toDto(debt);
        assertTrue(true);

    }
}
