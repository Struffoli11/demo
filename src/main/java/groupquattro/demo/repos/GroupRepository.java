package groupquattro.demo.repos;

import groupquattro.demo.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    Optional<Group> findGroupByIdGroup(String idGroup);

    Optional<Group> findGroupByGroupName(String groupName);
}
