package groupquattro.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtDto {

    private String id;
    private String expenceDescription;
    private double debt;
}
