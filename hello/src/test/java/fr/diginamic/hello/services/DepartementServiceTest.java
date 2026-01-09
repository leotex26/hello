package fr.diginamic.hello.services;

import fr.diginamic.hello.RecensementApplication;
import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Region;
import fr.diginamic.hello.repositories.DepartementRepository;
import fr.diginamic.hello.repositories.RegionRepository;
import fr.diginamic.hello.repositories.VilleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RecensementApplication.class)
@ActiveProfiles("test")
@Transactional
class DepartementServiceTest {

  @Autowired
  private DepartementService departementService;

  @Autowired
  private DepartementRepository departementRepository;

  @Autowired
  private RegionRepository regionRepository;

  @Autowired
  private VilleRepository villeRepository;

  @BeforeEach
  void init() {

    villeRepository.deleteAll();
    departementRepository.deleteAll();
    regionRepository.deleteAll();


    Region paysLoire = new Region("52", "Pays de la Loire");
    regionRepository.save(paysLoire);

    departementRepository.save(new Departement("49", "Maine-et-Loire", paysLoire));
    departementRepository.save(new Departement("85", "Vendée", paysLoire));

  }

  @Test
  void extraireToutes() {
    Iterable<Departement> departements = departementService.findAll();
    assertTrue(departements.iterator().hasNext());
  }

  @Test
  void findByCodeDepartementTest() throws VilleException {

    Departement departement = departementService.findByCodeDepartement("49");
    assertEquals("Maine-et-Loire", departement.getNom());

    VilleException exception = assertThrows(VilleException.class, () -> {
      departementService.findByCodeDepartement("98");
    });

    assertEquals(
      "Aucun département trouvé avec le code : 98",
      exception.getMessage()
    );
  }


}



