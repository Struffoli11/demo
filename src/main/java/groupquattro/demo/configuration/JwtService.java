package groupquattro.demo.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


// Questa classe serve per creare il jwt token,
// manipolarlo ed estrarre da esso informazioni sull'utente
@Service
public class JwtService {

  /*
      La secretKey è una chiave che permette di avere la certezza che il jwt token non sia stato
      modificato durane il percorso e che chi invia il jwt token sia chi afferma di essere.
      La dimensione di tale chiave dipende dai requisiti di sicurezza dell'algoritmo.
      Possiamo generare questa chiave automaticamente online attraverso vari tools.
  */
  @Value("${spring.jwt.secret}")
  private String SECRET_KEY;

  //Il token dura 30 min
  @Value("${spring.jwt.jwtExpirationTime}")
  private int JWT_EXPIRATION_TIME;

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  // Claims: impostazioni/attributi del token

  // Metodo per estratte un singolo claim specificato come parametro.
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    // Prima vengono estratti tutti i claims
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  //Questo metodo serve per generare un token da uno UserDetails senza pretese extra.
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  // A differenza del generateToken() precedente qui andiamo a settare le impostazioni del token.
  public String generateToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails
  ) {
    return buildToken(extraClaims, userDetails, JWT_EXPIRATION_TIME);
  }

  private String buildToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails,
          long expiration
  ) {
    // costruiamo l'oggetto che ritornerà.
    return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))  //setta per quanto tempo il toke è valido.
            .signWith(getSignInKey(), SignatureAlgorithm.HS256) //specifica quale secretKey usiamo.
                                                                // Il secondo parametro invece è l'algoritmo di firma.
            .compact();  //Metodo che genererà e restituirà il token.
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  // Decodifica della secretKey
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}

