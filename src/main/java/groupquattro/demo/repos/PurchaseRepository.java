package groupquattro.demo.repos;

import groupquattro.demo.classes.Purchase;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, ObjectId> {
    Optional<Purchase> findPurchaseByIdPurchase(String idPurchase);
}
