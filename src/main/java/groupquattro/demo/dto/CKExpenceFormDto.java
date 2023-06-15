package groupquattro.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CKExpenceFormDto {

    private String description;
    private String groupName;
    private String cost;
    private String date;
    private Map<String, String> payingMembers;
}
