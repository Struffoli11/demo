package groupquattro.demo.repos;

import groupquattro.demo.classes.Debt;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DebtRepository extends MongoRepository<Debt, ObjectId> {
    Optional<Debt> findDebtByIdDebt(String idDebt);
}
