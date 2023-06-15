package groupquattro.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupPageDto {

    private String idGroup;
    private String groupName;
    private String groupOwner;
    private List<CKExpenceSummaryDto> expences;
    private List<String> members;
//    private int numberOfExpences;

}
