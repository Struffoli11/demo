package groupquattro.demo.repos;

import groupquattro.demo.classes.Expence;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenceRepository extends MongoRepository<Expence, ObjectId> {
    Optional<Expence> findExpenceByIdExpence(String idExpence);
}
