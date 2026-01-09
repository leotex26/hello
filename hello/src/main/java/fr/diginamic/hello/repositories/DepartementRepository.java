package fr.diginamic.hello.repositories;

import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Integer> {


  Optional<Departement> findByCode(String code);
}

