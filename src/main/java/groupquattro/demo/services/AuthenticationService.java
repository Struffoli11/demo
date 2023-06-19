package groupquattro.demo.services;

import groupquattro.demo.configuration.JwtService;
import groupquattro.demo.dto.AuthenticationRequestDto;
import groupquattro.demo.dto.AuthenticationResponseDto;
import groupquattro.demo.dto.RegisterRequestDto;
import groupquattro.demo.exceptions.DuplicateResourceException;
import groupquattro.demo.model.Role;
import groupquattro.demo.model.User;
import groupquattro.demo.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/*
************************************************************************************************************************
    CLASSE CHE IMPLEMENTA LA REGISTRAZIONE, L'AUTENTICAZIONE, ECC.
************************************************************************************************************************
 */


@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  /*
      Questo metodo permette di creare un nuovo utente, salvarlo nel
      database e restiruire il token generato per tale utente.
   */
  public AuthenticationResponseDto register(RegisterRequestDto request) throws DuplicateResourceException {
    var user = User.builder()
        .email(request.getEmail())
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();
    
    if(repository.findUserByUsername(request.getUsername()).isPresent())
    	throw new DuplicateResourceException("Esiste già un utente con questo username.");
    
    if(repository.findUserByEmail( request.getEmail()).isPresent() )
    	throw new DuplicateResourceException("Esiste già un utente con questa email.");
    
    // Salva il nuovo utente creato nel db
    var savedUser = repository.save(user);

    // Genera un jwt token per il nuovo utente creato
    var jwtToken = jwtService.generateToken(user);
    
    /*
     	Prima di salvare il token sul db andiamo a revocare
     	tutti gli altri token associati all'utente in questione.
     	In questo modo per l'utente in questione
     	varrà solamente l'autenticazione attuale
    */
    
    return AuthenticationResponseDto.builder()
        .accessToken(jwtToken)
            .username(user.getUsername())
        .build();
  }

  public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );
    var user = repository.findUserByUsername(request.getUsername())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponseDto.builder()
        .accessToken(jwtToken)
            .username(jwtService.extractUsername(jwtToken))
        .build();
  }
  
  
  

}

