package fr.diginamic.hello.repositories;


import fr.diginamic.hello.model.Region;
import fr.diginamic.hello.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
  Optional<Utilisateur> findByUsername(String username);
}

