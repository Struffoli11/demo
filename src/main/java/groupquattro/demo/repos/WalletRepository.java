package groupquattro.demo.repos;

import groupquattro.demo.model.Wallet;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends MongoRepository<Wallet, ObjectId> {
    Optional<Wallet> findWalletByIdWallet(String idWallet);
}
