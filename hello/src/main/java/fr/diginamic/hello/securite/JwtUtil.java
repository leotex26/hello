package fr.diginamic.hello.securite;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
  private static final String SECRET = "secret-secret-secret-secret";
  public String generateToken(String username) {
    return Jwts.builder().setSubject(username).setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
      .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
      .compact();
  }
  public String extractUsername(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(SECRET.getBytes())
      .build().parseClaimsJws(token)
      .getBody().getSubject();
  }
}

