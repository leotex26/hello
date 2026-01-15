package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.Ville;
import fr.diginamic.hello.model.VilleDto;
import fr.diginamic.hello.model.mapper.VilleMapper;
import fr.diginamic.hello.services.DepartementService;
import fr.diginamic.hello.services.PaysService;
import fr.diginamic.hello.services.VilleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

/**
 * classe qui s'occupe des routes /villes
 */
@RestController
@RequestMapping("/villes")
public class VilleControleur implements IVilleControleur{


  @Autowired
  private VilleService villeService;
  @Autowired
  private DepartementService departementService;
  @Autowired
  private PaysService paysService;
  @Autowired
  private VilleMapper villeMapper;

  private static final Logger LOGGER = LoggerFactory.getLogger(VilleControleur.class);


  /**
   * méthode GET qui retourne la liste des villes
   * @return la liste des villes en base
   */
  //@Secured("ROLE_USER")
  @GetMapping()
  public Page<VilleDto> findAll(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size);

    return villeService.findAll(pageable)
      .map(villeMapper::toDto);
  }


  /**
   * méthode GET qui retourne une ville en fonction de son id
   * @param id : identifiant de la ville recherchée
   * @return le json de la ville recherché
   * @throws VilleException
   */
  @Secured("ROLE_USER")
  @GetMapping("/id/{id}")
  public ResponseEntity<VilleDto> rechercherVilleParId(@PathVariable int id) throws VilleException {
    VilleDto ville = villeMapper.toDto(villeService.findById(id));
    if (ville == null) {
      throw new VilleException("Ville n'existe pas");
    }
    return ResponseEntity.ok(ville);
  }



  /**
   * retourne une ville en fonction de son nom
   * @param nom : nom de la ville recherchée
   * @return la ville recherchéé
   * @throws VilleException
   */
  @Secured("ROLE_USER")
  @GetMapping("/search")
  public List<VilleDto> searchByNom(@RequestParam String nom) {
    return villeService.findByNomDebut(nom).stream()
      .map(villeMapper::toDto)
      .toList();
  }


  /**
   *  rechercher villes par population
   * @param min : population minimum
   * @param max : population maximum
   * @return une liste de villes trouvées en base
   * @throws VilleException
   */
  @Secured("ROLE_USER")
  @GetMapping("/searchByPopulation")
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


  /**
   * retourne une liste de villes qui ont une population supérieure à un minimum
   * @param min : seuil minimum de population
   * @return
   */
  @Secured("ROLE_USER")
  @GetMapping("/population/min")
  public List<VilleDto> populationMin(@RequestParam int min) {
    return villeService.findByPopulationMin(min).stream()
      .map(villeMapper::toDto)
      .toList();
  }

  /**
   * retourne une liste de villes qui ont une population entre un minimum et un maximum
   * @param min
   * @param max
   * @return
   */
  @Secured("ROLE_USER")
  @GetMapping("/population")
  public List<VilleDto> populationMinMax(
    @RequestParam int min,
    @RequestParam int max) {

    return villeService.findByPopulationMinMax(min, max).stream()
      .map(villeMapper::toDto)
      .toList();
  }

  /**
   * retourne
   * @param id
   * @param min
   * @param max
   * @return
   * @throws VilleException
   */
  @Secured("ROLE_USER")
  @GetMapping("/departement/{id}/population")
  public List<VilleDto> villesParDepartement(
    @PathVariable int id,
    @RequestParam int min,
    @RequestParam(required = false) Integer max) throws VilleException {

    List<Ville> villes = (max == null)
      ? villeService.findByDepartementMin(id, min)
      : villeService.findByDepartementMinMax(id, min, max);

    return villes.stream().map(villeMapper::toDto).toList();
  }


  /**
   * villes d'un département par page
   * @param id
   * @param n
   * @return
   * @throws VilleException
   */
  @Secured("ROLE_USER")
  @GetMapping("/departement/{id}/top")
  public List<VilleDto> topNVilles(
    @PathVariable int id,
    @RequestParam int n) throws VilleException {

    return villeService.findTopNVilles(id, n).stream()
      .map(villeMapper::toDto)
      .toList();
  }




  /**
   * permet d'ajouter une ville en base
   * @param villeDto : notre schema de ville
   * @return message de succès ou erreur
   * @throws VilleException
   */
  @Secured({"ROLE_USER","ROLE_ADMIN"})
  @PostMapping("/add")
  public ResponseEntity<String> ajouterVille(
    @Valid @RequestBody VilleDto villeDto
  ) throws VilleException {

    Ville ville = villeMapper.toBean(villeDto);

    villeService.creerVille(ville);
    return ResponseEntity.ok("Ville intégrée avec succès");
  }



  /**
   * méthode PUT qui prend une ville en paramètre et permet de modifier les données
   * d’une ville existante.
   * @param villeDto
   * @return
   * @throws VilleException
   */
  @Secured({"ROLE_USER","ROLE_ADMIN"})
  @PutMapping("/{id}")
  public ResponseEntity<VilleDto> modifierVilleParId(
    @PathVariable Integer id,
    @Valid @RequestBody VilleDto villeDto
  ) throws VilleException {

    Ville ville = villeMapper.toBean(villeDto);
    ville.setId(id);

    Ville villeSauvegardee = villeService.modifierVille(ville);

    return ResponseEntity.ok(villeMapper.toDto(villeSauvegardee));
  }

  /**
   * méthode DELETE qui permet de supprimer une ville en fonction de son id
   * @param id
   * @return
   * @throws VilleException
   */
  @Secured({"ROLE_USER","ROLE_ADMIN"})
  @DeleteMapping("/{id}")
  public ResponseEntity<String> supprimerVilleParId(@PathVariable("id") int id) throws VilleException {

    villeService.supprimerVille(id);
    return ResponseEntity.ok("ville supprimée avec succès");
  }




  /**
   * route de l'export csv
   * @param min
   * @return
   * @throws VilleException
   */
  @Secured("ROLE_USER")
  @GetMapping(value = "/exportCSV", produces = "text/csv")
  public ResponseEntity<byte[]> exportCSV(@RequestParam int min) throws VilleException {
    StringBuilder csv = villeService.findForExport(min);

    byte[] csvBytes = csv.toString().getBytes(StandardCharsets.UTF_8);

    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=villes.csv")
      .contentType(MediaType.parseMediaType("text/csv"))
      .body(csvBytes);
  }






}
