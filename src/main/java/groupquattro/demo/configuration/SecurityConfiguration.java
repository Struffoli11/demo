package groupquattro.demo.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;
  
  /* 
     Salvare il jwt: si fa su JavaScript nel dom (?), praticamente occorre memorizzare
  	 			il jwt token in modo che il browser se lo ricordi 
  */


  /*
      Questo bean è il responsabile della configurazione di tutta la sicurezza http della nostra applicazione.
  */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors().and().csrf()
        .disable()
        .authorizeHttpRequests()
        // Per accedere agli URL all'interno di questo metodo l'utente deve essere autenticato.
        .requestMatchers(
                "\"static\"immagini\"**",
                "\"static\"html\"**",
                "\"static\"js\"**",
                "/api/v1/auth/**"
        )
          .permitAll()
          
          /*
           		Per la gestione dei proprietari delle chiavi autorizzati ad accedere a
           		una determinata cassa non è fattibile usare i ruoli, occore gestirlo
           		a livello dei servizi.
           */

          /*
            Tutte le altre richieste (URL) non hanno bisogno dell'autenticazione per accedervi
            Fanno parte della White Listing.
         */
        .anyRequest()
          .authenticated()
        .and()
          .sessionManagement()

           // STATELESS: ogni volta la richiesta viene autenticata, dunque il suo stato non viene memorizato.
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()

        // Specificare a Spring quale authentication provider si vuole utilizzare.
        .authenticationProvider(authenticationProvider)

        /*
            Specificare a Spring di utilizzare il jwtAuthFilter.
            Più precisamente lo si vuole utilizzare prima dello UsernamePasswordAuthenticationFilter.
         */

        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .logout()
        /*
         * Per il logout Spring utilizza un URL predefinito "/api/v1/auth/logout" (richiesta POST)
         
         * !!Per effettuare correttamente il logout occorre autorizzarsi col jwt token!!
         */
        .logoutUrl("/api/v1/auth/Logout")
        .addLogoutHandler(logoutHandler)
        /*
         		logoutSuccessHandler è una lambda expression.
         		Si vuole pulire il SecurityContextHolder.
         		Infatti se l'utente si disconnette dobbiamo pulire il SecurityContextHolder
         		in modo che non possa accedere con il token attuale.
         		
         */
        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
    ;

    return http.build();
  }
}
