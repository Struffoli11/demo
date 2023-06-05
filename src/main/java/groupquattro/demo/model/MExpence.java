package groupquattro.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Data
@Document(collection = "milexpences")
@AllArgsConstructor
public class MExpence extends CKExpence{
    //    public CKExpence(double cost, Map<String, Double> payingMembers,
//        String description, Date date, String groupName, Chest chest)
    public MExpence(CKExpence sup){
        super(sup.getCost(), sup.getPayingMembers(),sup.getDescription(), sup.getDate(), sup.getGroupName(), sup.getChest(), sup.getDebtors());
    }

}
