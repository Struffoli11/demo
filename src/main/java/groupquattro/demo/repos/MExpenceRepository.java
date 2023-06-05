package groupquattro.demo.repos;

import groupquattro.demo.model.MExpence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MExpenceRepository extends MongoRepository<MExpence, String> {
    public Optional<MExpence> findExpenceById(String id);
}
