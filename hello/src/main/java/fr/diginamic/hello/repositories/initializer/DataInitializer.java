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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private VilleService villeService;

  @Autowired
  private DepartementService departementService;

  @Autowired
  private PaysService paysService;

//  @Override
//  @Transactional
//  public void run(String... args) throws Exception {
//
//    Pays pays = paysService.findOrCreate("France");
//
//    Departement d35 = departementService.create("35", "ILLE-ET-VILAINE");
//    Departement d29 = departementService.create("29", "FINISTERE");
//    Departement d56 = departementService.create("56", "MORBIHAN");
//
//    List<Ville> villes = List.of(
//      new Ville("Rennes", 300000, d35),
//      new Ville("Brest", 100000, d29),
//      new Ville("Quimper", 65000, d29),
//      new Ville("Vannes", 40000, d56),
//      new Ville("Saint-Malo", 55000, d35)
//    );
//
//    for (Ville ville : villes) {
//      if (!villeService.existsByNomAndDepartement(
//        ville.getNom(),
//        ville.getDepartement())) {
//
//        villeService.insertVille(ville);
//      }
//    }
//  }

  /**
   * implémentation avec des fichiers texte
   * @param args
   * @throws Exception
   */
  @Override
  @Transactional
  public void run(String... args) throws Exception {

    Pays pays = new Pays("FRANCE");
    paysService.insert(pays);

    InputStream is = getClass().getResourceAsStream("/datas/departement.txt");
    if (is == null) {
      System.out.println("Fichier introuvable !");
      return;
    }

    List<String> lines = new BufferedReader(new InputStreamReader(is))
      .lines()
      .collect(Collectors.toList());

    System.out.println("Nombre de lignes : " + lines.size());
    System.out.println("Première ligne : " + lines.get(0));

    for (String line : lines) {
      String[] tab = line.split(",");
      if (tab.length != 2) {
        continue;
      }
      departementService.create(tab[0].trim(), tab[1].trim().toUpperCase());
    }


    // implementation des villes

    InputStream input = getClass().getResourceAsStream("/datas/villes.txt");
    if (input == null) {
      System.out.println("Fichier introuvable !");
      return;
    }

    List<String> linesCity = new BufferedReader(new InputStreamReader(input))
      .lines()
      .collect(Collectors.toList());


    System.out.println("Nombre de lignes : " + linesCity.size());
    System.out.println("Première ligne : " + linesCity.get(0));

    for (String line : linesCity) {
      String[] tab = line.split(",");
      if (tab.length != 3) {
        continue;
      }
      Departement departement = departementService.findByCodeDepartement(tab[2].trim());
      if (departement == null) {
        continue;
      }
      Ville ville = new Ville(tab[0].trim().replace("\"",""),Integer.parseInt(tab[1].trim()),departement);
      villeService.insertVille(ville);
    }






  }

}
