package fr.diginamic.hello.repositories;

import fr.diginamic.hello.model.Pays;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaysRepository extends JpaRepository<Pays, Integer> {
}
