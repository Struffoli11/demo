package groupquattro.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    private ObjectId id;

    private String groupName;

    private String idGroup;

    private List<String> members;

    @DocumentReference
    private List<RomanaExpence> expences;

    public Group(String groupName, String idGroup, List<String> members) {
        this.groupName = groupName;
        this.idGroup = idGroup;
        this.members = members;
    }
}
