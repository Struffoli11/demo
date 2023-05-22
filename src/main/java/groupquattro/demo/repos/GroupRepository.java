package groupquattro.demo.repos;

import groupquattro.demo.model.Group;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends MongoRepository<Group, ObjectId> {
    Optional<Group> findGroupByIdGroup(String idGroup);

    Optional<Group> findGroupByGroupName(String groupName);
}
