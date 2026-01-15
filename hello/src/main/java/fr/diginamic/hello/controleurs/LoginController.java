package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.securite.JwtUtil;
import fr.diginamic.hello.securite.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtUtil jwtUtil;

  @PostMapping("/login")
  public String login(@RequestBody LoginRequest req) {
// Déclenche la vérification du mot de passe avec UserDetailsService
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(),
      req.getPassword()));
// Retourne un JWT
    return jwtUtil.generateToken(req.getUsername());
  }
}
