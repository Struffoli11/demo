package groupquattro.demo.repos;

import groupquattro.demo.model.RomanaExpence;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RomanaExpenceRepository extends MongoRepository<RomanaExpence, ObjectId> {
    Optional<RomanaExpence> findRomanaExpenceById(String id);

//    @Query(value = "{ 'description' : ?0 }", fields = "{ 'description' : 1, 'date' : 1 }")
//    List<RomanaExpence> findByGroupNameIncludeDescriptionAndDate(String description);

    @Query(value = "{ 'groupName' : ?0 }")
    List<RomanaExpence> findRomanaExpenceByGroupName(String groupName);
}
