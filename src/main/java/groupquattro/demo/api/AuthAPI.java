package groupquattro.demo.api;

import groupquattro.demo.configuration.LogoutService;
import groupquattro.demo.dto.AuthenticationRequestDto;
import groupquattro.demo.dto.ServerResponse;
import groupquattro.demo.dto.RegisterRequestDto;
import groupquattro.demo.exceptions.DuplicateResourceException;
import groupquattro.demo.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthAPI {

  private final AuthenticationService service;
  @Autowired
  LogoutService logoutService;

  @PostMapping("/register")
  public ResponseEntity<?> register(
      @RequestBody RegisterRequestDto request
  ) throws DuplicateResourceException{
      try{
          URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                  .path("/{username}")
                  .buildAndExpand(request.getUsername())
                  .toUri();
          //le info sull'utente potrannno essere richieste a questo url: api/v1/users/{username}
          ResponseEntity<?> response = ResponseEntity.ok(service.register(request));
          return response;
      }catch(DuplicateResourceException e ){
          return ResponseEntity.status(400).body(new ServerResponse(e.getLocalizedMessage()));
      }
  }
  @PostMapping("/authenticate")
  public ResponseEntity<?> authenticate(
      @RequestBody AuthenticationRequestDto request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }
  
  @PostMapping("/Logout")
  public ResponseEntity<?> doLogout( HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) {
	  logoutService.logout(request, response, authentication);
      log.info("User " + authentication.getName() + " logged out");
	  return ResponseEntity.ok().body(new ServerResponse("User has been logged out successfully"));
  }
}
