package groupquattro.demo.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {
	
	@Autowired
	private TokenRepository tokenRepository;
	
	public void createToken(Token token) {
		tokenRepository.save(token);
	}
	
	public boolean isTokenPresent(String tokenJWT) {
		 Optional<Token> optional = tokenRepository.findTokenByJwt(tokenJWT);
		 
		 if(optional.isPresent()) {
			 return true; 
		 }
		 return false;
	}

}
