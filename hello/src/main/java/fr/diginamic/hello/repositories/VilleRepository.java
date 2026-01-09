package fr.diginamic.hello.repositories;

import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Ville;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VilleRepository extends JpaRepository<Ville, Integer> {

  /** Recherche de toutes les villes dont le nom commence par une chaine de caractères données*/
  List<Ville> findByNomStartingWithIgnoreCase(String nom);

  /** Recherche de toutes les villes dont la population est supérieure à min (paramètre de
   type int). Les villes sont retournées par population descendante */
  List<Ville> findByPopulationGreaterThanOrderByPopulationDesc(int min);

   /** Recherche de toutes les villes dont la population est supérieure à min et inférieure à
   max. Les villes sont retournées par population descendante.*/
   List<Ville> findByPopulationBetweenOrderByPopulationDesc(int populationAfter, int populationBefore);

  /** Recherche de toutes les villes d’un département dont la population est supérieure à
   min (paramètre de type int). Les villes sont retournées par population descendante.*/
  List<Ville> findByDepartementAndPopulationGreaterThanOrderByPopulationDesc(Departement departement, int min);

  /** Recherche de toutes les villes d’un département dont la population est supérieure à
  min et inférieure à max. Les villes sont retournées par population descendante */
  List<Ville> findByDepartementAndPopulationBetweenOrderByPopulationDesc(Departement departement, int populationAfter, int populationBefore);

  /** Recherche des n villes les plus peuplées d’un département donné (n est aussi un
   paramètre) */
  List<Ville> findByDepartementOrderByPopulationDesc(
    Departement departement,
    Pageable pageable
  );





   }
