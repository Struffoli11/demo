package groupquattro.demo.token;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String>{
	Optional<Token> findTokenById(String id);
	
	Optional<Token> findTokenByJwt(String jwt);
	
	

}
