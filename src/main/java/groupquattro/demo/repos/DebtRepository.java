package groupquattro.demo.repos;

import groupquattro.demo.model.Debt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DebtRepository extends MongoRepository<Debt, String> {
    public Optional<Debt> findDebtById(String id);

    @Query(value = "{ 'expenceId' : ?0 }")
    Optional<List<Debt>> getDebtsByExpenceId(String expenceId);
}
