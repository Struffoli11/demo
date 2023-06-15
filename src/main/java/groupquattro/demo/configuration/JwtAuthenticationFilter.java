package groupquattro.demo.configuration;

import groupquattro.demo.token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
//@RequiredArgsConstructor //crea un costruttore usando qualsiasi parametro tra le var d'ist final
public class JwtAuthenticationFilter extends OncePerRequestFilter {


@Autowired  private JwtService jwtService;
 @Autowired private UserDetailsService userDetailsService;
@Autowired private TokenService tokenService;

  /*
      filter chain=catena di filtri da eseguire
      i 3 parametri non devono essere nulli (@NonNull)
  */
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    if (request.getServletPath().contains("/api/v1/auth")) {
      filterChain.doFilter(request, response);
      return;
    }
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String username;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {  //jwt deve iniziare sempre con "Bearer "
      // se si entra nell' if occorre chiamare la filterChain
      // e semplicemente passare al filtro successivo
      filterChain.doFilter(request, response);
      return; //ovviamente non continueremo con il resto del codice, quindi si invonca il return
    }

    // Estraiamo l'authHeader(intestazione) dal jwt token.
    // Partiamo dal carattere in posizione 7 poichè dobbiamo
    // estrarre da dopo "Bearer " che ha proprio 7 caratteri
    jwt = authHeader.substring(7);
    
    if(tokenService.isTokenPresent(jwt)) {
    	throw new IOException("token in black list");
    }
    username = jwtService.extractUsername(jwt);

    // Verifico che la mail non sia nulla e che l'utente non sia stato già autenticato.
    // Ovviamente se l'utente è già autenticato non occorre effettuare nuoovamente i controlli e
    // aggiornare il SecurityConvtextHolder.
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

      /* 
        	Controlla se il jwt token è valido o no.
       		isTokenValid serve per controllare che il token non sia expired o revoked,
       		senza questo controllo un token non valido potrebbe autenticare un utente.
      */
      
      if (jwtService.isTokenValid(jwt, userDetails)) {
        // Se il jwt token bisogna aggiornare il SecurityContextHolder
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,  //non abbiamo le creenziali ecco perchè qui si passa null
            userDetails.getAuthorities()
        );
        // Posso fornigli ulteriori details(dettagli) con .setDetails
        authToken.setDetails(
            // Costruisco i details della nostra richiesta http
            new WebAuthenticationDetailsSource().buildDetails(request)
        );
        // Qui effettivamente si aggiorna il SecurityContextHoldet passandogli come parametro l'authToken creato.
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    // Occorre sempre passare al filtro successivo della filterChain.
    filterChain.doFilter(request, response);
  }
}

