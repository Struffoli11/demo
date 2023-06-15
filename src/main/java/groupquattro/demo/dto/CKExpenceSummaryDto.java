package groupquattro.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CKExpenceSummaryDto {

    private String id;
    private String description;
    private String cost;
    private String date;
    private ChestDto chest;
    private Map<String, String> payingMembers;
}
