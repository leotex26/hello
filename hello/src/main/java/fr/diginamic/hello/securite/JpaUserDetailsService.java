package fr.diginamic.hello.securite;

import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.Role;
import fr.diginamic.hello.model.Utilisateur;
import fr.diginamic.hello.repositories.RoleRepository;
import fr.diginamic.hello.repositories.UtilisateurRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class JpaUserDetailsService implements UserDetailsService {
  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private UtilisateurRepository utilisateurRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Transactional
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return utilisateurRepository.findByUsername(username).get();
  }

  @Transactional
  @PostConstruct
  public void chargerUtilisateur(){

    List<Utilisateur> users = new ArrayList<>();

    Role roleUser = new Role("ROLE_USER");
    Role roleAdmin = new Role("ROLE_ADMIN");

    users.add(new Utilisateur("gdupont", encoder.encode("user1234"),roleUser ));
    users.add(new Utilisateur("aduval", encoder.encode("admin1234"), roleUser , roleAdmin));

    roleRepository.save(roleUser);
    roleRepository.save(roleAdmin);

    for (Utilisateur user : users){
      utilisateurRepository.save(user);
    }

  }


}

