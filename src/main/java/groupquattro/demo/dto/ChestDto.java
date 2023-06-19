package groupquattro.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChestDto {
    private String id;
    private String currentValue;
    private String percentage;
    private String max_amount;
    private String isOpen;
}
