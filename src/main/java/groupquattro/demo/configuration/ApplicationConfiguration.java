package groupquattro.demo.configuration;

import groupquattro.demo.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

  private final UserRepository repository;

  // Questo bean serve per implementare l'interfaccia UserDetailsService,
  // così NON utilizzeremo l'implementazione di default fornita da Spring.
  @Bean
  public UserDetailsService userDetailsService() {
    /* Lamba expression
    // username è passato come parametro.
    // Quello che vogliamo fare è recuperare l'utente dal db
    // In pratica andiamo a implementare il metodo loadBy...
    // dell'interfaccia UserDetailsService.*/
    return username -> repository.findUserByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  /*
      Questo authentication provider è l'oggetto di accesso ai dati,
      è responsabile di recuperare i details dell'utente e di codificare la password.
  */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    // DaoAuthenticationProvider è un tipo fornito da Spring.
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    /*
        Specifichiamo quale userDetailsService si vuole utilizzare per recuperare le informazioni
        sull'utente, visto che potrebbero esserci diverse implementazione di UserDetailsService.
     */
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  /*
      L'AuthenticationManager è resposnabile della gestione dell'autenticazione.
      L'AuthenticationManager ha una serie di metodi, uno di questi è un metodo che consente di
      autenticarci o in base all'utente o utilizzanso solamente il suo username e la sua password.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    /*
        Essendo config di tipo AuthenticationConfguration contiene già le informazioni
        sull'AuthenticationManager quindi restituirò solo config.getAuthenticationManager().
        Quondi qui stiamo usando un'implementazione predefinita di Spring Boot, più che sufficiente
        per quello che dobbiamo fare.
     */
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
