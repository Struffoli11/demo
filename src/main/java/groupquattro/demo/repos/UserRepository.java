package groupquattro.demo.repos;

import groupquattro.demo.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findUserByUsername(String username);

    @Query(value = "{ 'email' : ?0}")
    Optional<User> findUserByEmail(String email);
}
