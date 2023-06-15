package groupquattro.demo.configuration;

import groupquattro.demo.token.Token;
import groupquattro.demo.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

	@Autowired
	JwtService service;
	
	@Autowired
	TokenService tokenService;
	
  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) {
	  String header=request.getHeader("Authorization");
	  String jwt = header.substring(7);//jwt token
	  Token token = Token.builder()
			  .timeToLive(service.extractExpiration(jwt))
			  .jwt(jwt).build();
	  tokenService.createToken(token);
  }
}
