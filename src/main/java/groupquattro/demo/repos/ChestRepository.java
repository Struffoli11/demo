package groupquattro.demo.repos;

import groupquattro.demo.model.Chest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChestRepository extends MongoRepository<Chest, String> {
}
