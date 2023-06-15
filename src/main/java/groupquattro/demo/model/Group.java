package groupquattro.demo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "groups")
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class Group {
    @Id
    private String idGroup;
    @NonNull
    private String groupName;
    private List<String> members;
    @DocumentReference(collection = "ckexpences")
    private List<CKExpence> expences;
    @NonNull
    private String groupOwner;


    public Group(String groupName, String groupOwner) {
        this.groupName = groupName;
        this.groupOwner = groupOwner;
        this.members = new ArrayList<String>();
        this.members.add(groupOwner);
    }

    public Group( String groupName, String groupOwner, List<String> members) {
        this.groupName = groupName;
        this.members = members;
        this.groupOwner = groupOwner;
    }
}
