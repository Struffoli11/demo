package groupquattro.demo.repos;

import groupquattro.demo.model.Key;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface KeyRepository extends MongoRepository<Key, ObjectId> {

}
