package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Pays;
import fr.diginamic.hello.model.Ville;
import fr.diginamic.hello.model.VilleDto;
import fr.diginamic.hello.model.mapper.VilleMapper;
import fr.diginamic.hello.services.DepartementService;
import fr.diginamic.hello.services.PaysService;
import fr.diginamic.hello.services.VilleService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
  @Autowired
  private VilleMapper villeMapper;


  /**
   * méthode GET qui retourne la liste des villes
   * @return la liste des villes en base
   */
  @GetMapping
  public List<VilleDto> findAll() {
    List<VilleDto> villesDto = new ArrayList<>();

    for (Ville ville : villeService.findAll()) {
      villesDto.add(villeMapper.toDto(ville));
    }

    return villesDto;
  }

  /**
   * méthode GET qui retourne une ville en fonction de son id
   * @param id : identifiant de la ville recherchée
   * @return le json de la ville recherché
   * @throws VilleException
   */
  @GetMapping("/id/{id}")
  public ResponseEntity<VilleDto> rechercherVilleParId(@PathVariable int id) throws VilleException {
    VilleDto ville = villeMapper.toDto(villeService.findById(id));
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

    List<VilleDto> villesConcerned = villeService.findByBegining(nom).stream().map(villeMapper::toDto).toList();

    if (villesConcerned.isEmpty()) {
      throw new VilleException(" Aucune ville dont le nom commence par " + nom + " n’a été trouvée\n");
    }
    return ResponseEntity.ok(villesConcerned);
  }


  /**
   * permet d'ajouter une ville en base
   * @param villeDto : notre schema de ville
   * @return message de succès ou erreur
   * @throws VilleException
   */
  @PostMapping("/add")
  public ResponseEntity<String> ajouterVille(
    @Valid @RequestBody VilleDto villeDto
  ) throws VilleException {

    villeService.creerVille(villeDto.getNom(), villeDto.getPopulation(), villeDto.getCodeDepartement(), villeDto.getIdDepartement());
    return ResponseEntity.ok("Ville intégrée avec succès");
  }



  /**
   * méthode PUT qui prend une ville en paramètre et permet de modifier les données
   * d’une ville existante.
   * @param villeDto
   * @return
   * @throws VilleException
   */
  @PutMapping("/{id}")
  public ResponseEntity<VilleDto> modifierVilleParId(
    @PathVariable Integer id,
    @Valid @RequestBody VilleDto villeDto
  ) throws VilleException {
    Ville villeModifiee = villeMapper.toBean(villeDto);
    Ville villeSauvegardee = villeService.modifierVille(id, villeModifiee);
    return ResponseEntity.ok(villeMapper.toDto(villeSauvegardee));
  }

  /**
   * méthode DELETE qui permet de supprimer une ville en fonction de son id
   * @param id
   * @return
   * @throws VilleException
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<String> supprimerVilleParId(@PathVariable("id") int id) throws VilleException {

    villeService.supprimerVille(id);
    return ResponseEntity.ok("ville supprimée avec succès");
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

    List<VilleDto> villesConcerned = villeService.getVillesBetweenMinAndMax(min, max).stream().map(villeMapper::toDto).toList();

    if (villesConcerned.isEmpty() && max == null) {
      throw new VilleException("Aucune ville n’a une population superieur à " + min);
    } else if (villesConcerned.isEmpty()) {
      throw new VilleException("Aucune ville n’a une population entre " + min + " et " + max);
    }

    return ResponseEntity.ok(villesConcerned);
  }




}
