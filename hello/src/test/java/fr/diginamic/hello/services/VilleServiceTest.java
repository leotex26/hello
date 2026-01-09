package fr.diginamic.hello.services;

import fr.diginamic.hello.RecensementApplication;
import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Region;
import fr.diginamic.hello.model.Ville;
import fr.diginamic.hello.repositories.DepartementRepository;
import fr.diginamic.hello.repositories.VilleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RecensementApplication.class)
@ActiveProfiles("test")
@Transactional
class VilleServiceTest {

  @Autowired
  private VilleService villeService;

  @Autowired
  private VilleRepository villeRepository;

  @Autowired
  private DepartementRepository departementRepository;

  @BeforeEach
  void init() throws VilleException {
    // Supprimer toutes les villes et départements existants pour un test propre
    villeRepository.deleteAll();
    departementRepository.deleteAll();

    // Créer région et département
    Region paysLoire = new Region("52", "Pays de la Loire");
    departementRepository.save(new Departement("49", "Maine-et-Loire", paysLoire));
    departementRepository.save(new Departement("85", "Vendée", paysLoire));

    Departement maineLoire = departementRepository.findByCode("49").orElseThrow();

    // Ajouter des villes
    villeRepository.save(new Ville("Angers", 142_000, maineLoire));
    villeRepository.save(new Ville("Cholet", 55_000, maineLoire));
    villeRepository.save(new Ville("Zama", 148_000, maineLoire));
  }

  @Test
  void extraireToutes() {
    Iterable<Ville> villes = villeService.findAll();
    assertTrue(villes.iterator().hasNext());
  }

  @Test
  void findByBeginingTest() {
    List<Ville> villes = villeService.findByBegining("Chol");
    assertEquals(1, villes.size());
    assertEquals("Cholet", villes.get(0).getNom());
    assertEquals(55_000, villes.get(0).getPopulation());
  }

  @Test
  void getTopNVillesByDepartementTest() throws VilleException {
    List<Ville> villes = villeService.getTopNVillesByDepartement("49", 2);
    assertEquals(2, villes.size());
    assertEquals("Zama", villes.get(0).getNom());
    assertEquals("Angers", villes.get(1).getNom());
  }

  @Test
  void getVillesByPopulationAndDepartementTest() throws VilleException {
    List<Ville> villes = villeService.getVillesByPopulationAndDepartement("49", 100_000, 150_000);
    assertEquals(2, villes.size());
    for (Ville v : villes) {
      assertTrue(v.getPopulation() > 100_000);
      assertTrue(v.getPopulation() < 150_000);
      assertEquals("Maine-et-Loire", v.getDepartement().getNom());
    }
  }
}
