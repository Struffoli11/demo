package groupquattro.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Group {
    @Id
    private String idGroup;

    private String groupName;

    private List<String> members;

    private List<String> expences;

    /**
     * the user that creates the group
     */
    @DocumentReference
    private User groupOwner;

    public Group(String groupName, User groupOwner) {
        this.groupName = groupName;
        this.groupOwner = groupOwner;
        this.members = new ArrayList<String>();
        members.add(groupOwner.getUsername());
    }

    public Group( String groupName, User groupOwner, List<String> members) {
        this.groupName = groupName;
        this.members = members;
        this.groupOwner = groupOwner;
    }
}
