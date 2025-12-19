package fr.diginamic.hello.repositories.initializer;

import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Pays;
import fr.diginamic.hello.model.Ville;
import fr.diginamic.hello.services.DepartementService;
import fr.diginamic.hello.services.PaysService;
import fr.diginamic.hello.services.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private VilleService villeService;

  @Autowired
  private DepartementService departementService;

  @Autowired
  private PaysService paysService;

  @Override
  @Transactional
  public void run(String... args) throws Exception {

    Pays pays = paysService.findOrCreate("France");

    Departement d35 = departementService.create("35", "ILLE-ET-VILAINE");
    Departement d29 = departementService.create("29", "FINISTERE");
    Departement d56 = departementService.create("56", "MORBIHAN");

    List<Ville> villes = List.of(
      new Ville("Rennes", 300000, d35),
      new Ville("Brest", 100000, d29),
      new Ville("Quimper", 65000, d29),
      new Ville("Vannes", 40000, d56),
      new Ville("Saint-Malo", 55000, d35)
    );

    for (Ville ville : villes) {
      if (!villeService.existsByNomAndDepartement(
        ville.getNom(),
        ville.getDepartement())) {

        villeService.insertVille(ville);
      }
    }
  }
}
