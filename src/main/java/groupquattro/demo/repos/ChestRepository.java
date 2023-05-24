package groupquattro.demo.repos;

import groupquattro.demo.model.Chest;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChestRepository extends MongoRepository<Chest, ObjectId> {
}
