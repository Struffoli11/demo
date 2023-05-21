package groupquattro.demo.repos;

import groupquattro.demo.model.RomanaExpence;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DebtRepository extends MongoRepository<RomanaExpence.Debt, ObjectId> {
    public Optional<RomanaExpence.Debt> findDebtById(String id);
}
