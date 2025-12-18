package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Pays;
import fr.diginamic.hello.model.Ville;
import fr.diginamic.hello.services.DepartementService;
import fr.diginamic.hello.services.PaysService;
import fr.diginamic.hello.services.VilleService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * classe qui s'occupe des routes /villes
 */
@RestController
@RequestMapping("/villes")
public class VilleControleur {


  @Autowired
  private VilleService villeService;
  @Autowired
  private DepartementService departementService;
  @Autowired
  private PaysService paysService;

  /**
   * methode d'initialisation
   * @throws VilleException
   */
  @PostConstruct
  void initData() throws VilleException {

    Pays pays = paysService.findOrCreate("France");

    Departement d35 = departementService.findOrCreate("35", "ILLE-ET-VILAINE", pays);
    Departement d29 = departementService.findOrCreate("29", "FINISTERE", pays);
    Departement d56 = departementService.findOrCreate("56", "MORBIHAN", pays);

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

  /**
   * méthode GET qui retourne la liste des villes
   * @return la liste des villes en base
   */
  @GetMapping()
  public List<Ville> findAll() {
    return villeService.findAll();
  }

  /**
   * méthode GET qui retourne une ville en fonction de son id
   * @param id : identifiant de la ville recherchée
   * @return le json de la ville recherché
   * @throws VilleException
   */
  @GetMapping("/id/{id}")
  public ResponseEntity<Ville> rechercherVilleParId(@PathVariable int id) throws VilleException {
    Ville ville = villeService.findById(id);
    if (ville == null) {
      throw new VilleException("Ville n'existe pas");
    }
    return ResponseEntity.ok(ville);
  }



  /**
   * méthode GET qui retourne une ville en fonction de son nom
   * @param nom : nom de la ville recherchéé
   * @return la ville recherchéé
   * @throws VilleException
   */
  @GetMapping("/nom/{nom}")
  public ResponseEntity<?> searchVilleByBegining(@PathVariable String nom) throws VilleException {

    List<Ville> villesConcerned = villeService.findByBegining(nom);

    if (villesConcerned.isEmpty()) {
      throw new VilleException(" Aucune ville dont le nom commence par " + nom + " n’a été trouvée\n");
    }
    return ResponseEntity.ok(villesConcerned);
  }





  /**
   * méthode POST qui prend une nouvelle ville en paramètre et la met en base de
   * données
   * @param nom
   * @param population
   * @param codeDepartement
   * @return
   * @throws VilleException
   */
  @PostMapping("/add")
  public ResponseEntity<String> ajouterVille(@RequestParam String nom, @RequestParam int population, @RequestParam String codeDepartement) throws VilleException {
    System.out.println("ajouterVille");

    Departement departement = departementService.findByCodeDepartement(codeDepartement);
    Ville ville = new Ville(nom, population, departement);
    villeService.insertVille(ville);

    return ResponseEntity.ok("ville integrée avec succès");
  }



  /**
   * méthode PUT qui prend une ville en paramètre et permet de modifier les données
   * d’une ville existante.
   * @param id
   * @param villeModifiee
   * @param bindingResult
   * @return
   * @throws VilleException
   */
  @PutMapping("/{id}")
  public ResponseEntity<Ville> modifierVilleParId(
    @PathVariable Integer id,
    @Valid @RequestBody Ville villeModifiee, // RequestBody >
    BindingResult bindingResult
  ) throws VilleException {

    if (bindingResult.hasErrors()) {
      throw new VilleException("Attributs de ville invalides");
    }

    Ville villeExistante = villeService.findById(id);

    villeExistante.setNom(villeModifiee.getNom());
    villeExistante.setPopulation(villeModifiee.getPopulation());

    Ville villeSauvegardee = villeService.save(villeExistante);

    return ResponseEntity.ok(villeSauvegardee);
  }

  /**
   * méthode DELETE qui permet de supprimer une ville en fonction de son i
   * @param id
   * @return
   * @throws VilleException
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<String> supprimerVilleParId(@PathVariable("id") int id) throws VilleException {

    Ville v = villeService.findById(id);

    if (v != null) {
      villeService.remove(v);
      return ResponseEntity.ok("supprimer avec succès");
    }
    throw new VilleException("ville non trouvé");
  }

  /**
   *  rechercher villes par population
   * @param min : population minimum
   * @param max : population maximum
   * @return une liste de villes trouvées en base
   * @throws VilleException
   */
  @GetMapping("/search")
  public ResponseEntity<?> searchByPopulation(
    @RequestParam int min,
    @RequestParam(required = false) Integer max) throws VilleException {

    List<Ville> villesConcerned = villeService.getVillesBetweenMinAndMax(min, max);

    if (villesConcerned.isEmpty() && max == null) {
      throw new VilleException("Aucune ville n’a une population superieur à " + min);
    } else if (villesConcerned.isEmpty()) {
      throw new VilleException("Aucune ville n’a une population entre " + min + " et " + max);
    }

    return ResponseEntity.ok(villesConcerned);
  }




}
