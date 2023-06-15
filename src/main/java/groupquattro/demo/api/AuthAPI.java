package groupquattro.demo.api;

import groupquattro.demo.configuration.LogoutService;
import groupquattro.demo.dto.AuthenticationRequestDto;
import groupquattro.demo.dto.RegisterRequestDto;
import groupquattro.demo.exceptions.DuplicateResourceException;
import groupquattro.demo.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthAPI {

  private final AuthenticationService service;
  @Autowired
  LogoutService logoutService;

  @PostMapping("/register")
  public ResponseEntity<?> register(
      @RequestBody RegisterRequestDto request
  ) throws DuplicateResourceException{
      try{
          return ResponseEntity.ok(service.register(request));
      }catch(DuplicateResourceException e ){
          return ResponseEntity.status(400).body(e.getLocalizedMessage());
      }
  }
  @PostMapping("/authenticate")
  public ResponseEntity<?> authenticate( //Punto interrogativo:non si da una info precisa su cosa restituisci
      @RequestBody AuthenticationRequestDto request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }
  
  @PostMapping("/Logout")
  public ResponseEntity<?> doLogout( HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) {
	  logoutService.logout(request, response, authentication);
	  return ResponseEntity.ok().body("");
  }

  
}
