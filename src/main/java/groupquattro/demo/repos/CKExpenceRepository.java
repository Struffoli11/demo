package groupquattro.demo.repos;

import groupquattro.demo.model.CKExpence;
import groupquattro.demo.model.RomanaExpence;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CKExpenceRepository extends MongoRepository<CKExpence, ObjectId> {

    @Query(value = "{ 'groupName' : ?0 }")
    List<CKExpence> findByGroupName(String groupName);

    Optional<CKExpence> findCKExpenceById(String idExpence);
}
