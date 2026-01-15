package fr.diginamic.hello.services;

import fr.diginamic.hello.Validator.VilleValidator;
import fr.diginamic.hello.controleurs.VilleControleur;
import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Ville;
import fr.diginamic.hello.model.VilleDto;
import fr.diginamic.hello.model.mapper.VilleMapper;
import fr.diginamic.hello.repositories.VilleDao;
import fr.diginamic.hello.repositories.VilleRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VilleService implements IVilleService{

  @Autowired
  VilleValidator villeValidator;

  @Autowired
  IDepartementService departementService;

  @Autowired
  VilleDao villeDao;

  @Autowired
  VilleMapper villeMapper;

  @Autowired
  VilleRepository villeRepository;

  @Autowired
  private PaysService paysService;

  private static final Logger LOGGER = LoggerFactory.getLogger(VilleService.class);


  @Transactional
  public Ville updateVille(Ville ville) throws VilleException {

    Errors errors = new BeanPropertyBindingResult(ville, "ville");
    villeValidator.validate(ville, errors);

    if (errors.hasErrors()) {
      throw new VilleException("Attributs invalides");
    }

    if (!villeRepository.existsById(ville.getId())) {
      throw new VilleException("Ville inexistante");
    }

    return villeRepository.save(ville);
  }

  @Transactional
  public Ville insertVille(Ville ville) throws VilleException {
    Errors errors = new BeanPropertyBindingResult(ville, "ville");
    villeValidator.validate(ville, errors);

    if (errors.hasErrors()) {
      throw new VilleException("Attributs de ville invalides");
    }

    return villeRepository.save(ville);
  }


  public List<Ville> getVillesBetweenMinAndMax(int min, Integer max) {


    return villeRepository.findAll().stream()
      .filter(v -> v.getPopulation() >= min)
      .filter(v -> max == null || v.getPopulation() <= max)
      .toList();
  }

  @Transactional
  public Ville save(Ville ville) {
    return villeRepository.save(ville);
  }


  public Ville findById(int id) {
    return villeRepository.findById(id).orElse(null);
  }

  public List<Ville> findAll() {
    return villeRepository.findAll();
  }

  @Transactional
  public void remove(Ville v) {
    villeRepository.delete(v);
  }

  public boolean existsByNomAndDepartement(String nom, Departement departement) {
    List<Ville> villes = findAll();
    Long counter = villes.stream().filter(v -> v.getNom().equals(nom) && v.getDepartement().equals(departement)).count();
    return counter > 0;
  }


  public List<Ville> findByBegining(String nom) {
    List<Ville> villes = findAll();
    return villes.stream().filter(v -> v.getNom().toLowerCase().startsWith(nom.toLowerCase())).collect(Collectors.toList());
  }

    //villeService.creerVille(villeDto.getNom(), villeDto.getPopulation(), villeDto.getCodeDepartement(), villeDto.getIdDepartement());
  @Transactional
  public Ville creerVille(Ville ville) throws VilleException {

    Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();

    if (authentication.isAuthenticated()){
      ville.setDateMaj(LocalDateTime.now());
      ville.setUserMaj(authentication.getName());
      LOGGER.info("Ville "+ ville.getNom() +" mis à jour par :"+ authentication.getName());
    }else{
      ville.setDateMaj(LocalDateTime.now());
      ville.setUserMaj("Système");
      LOGGER.info("Ville "+ ville.getNom() +" mis à jour par : Système");
    }

    Errors errors = new BeanPropertyBindingResult(ville, "ville");
    villeValidator.validate(ville, errors);
    if (errors.hasErrors()) {
      throw new VilleException("Attributs de ville invalides");
    }

    return villeRepository.save(ville);
  }

  @Transactional
  public Ville modifierVille(Ville villeModifiee) throws VilleException {

    Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();

    if (authentication.isAuthenticated()) {
      villeModifiee.setDateMaj(LocalDateTime.now());
      villeModifiee.setUserMaj(authentication.getName());
      LOGGER.info("Ville " + villeModifiee.getNom() + " mise à jour par : " + authentication.getName());
    } else {
      villeModifiee.setDateMaj(LocalDateTime.now());
      villeModifiee.setUserMaj("Système");
      LOGGER.info("Ville " + villeModifiee.getNom() + " mise à jour par : Système");
    }

    Ville villeExistante = findById(villeModifiee.getId());
    if (villeExistante == null) {
      throw new VilleException("Ville inexistante avec id " + villeModifiee.getId());
    }

    Errors errors = new BeanPropertyBindingResult(villeModifiee, "ville");
    villeValidator.validate(villeModifiee, errors);
    if (errors.hasErrors()) {
      throw new VilleException("Attributs de ville invalides");
    }

    villeExistante.setNom(villeModifiee.getNom());
    villeExistante.setPopulation(villeModifiee.getPopulation());
    villeExistante.setDepartement(villeModifiee.getDepartement());

    return villeRepository.save(villeExistante);
  }

  @Transactional
  public void supprimerVille(Integer id) throws VilleException {
    Ville ville = findById(id);
    if (ville == null) {
      throw new VilleException("Ville non trouvée avec id " + id);
    }
    villeRepository.delete(ville);
  }


  /** Retourne les n plus grandes villes d'un département */
  @Transactional
  public List<Ville> getTopNVillesByDepartement(String codeDepartement, int n) throws VilleException {
    Departement dep = departementService.findByCodeDepartement(codeDepartement);
    return findByDepartementOrderByPopulationDesc(dep)
      .stream()
      .limit(n)
      .collect(Collectors.toList());
  }

  /** Retourne les villes d'un département selon une population min et max */
  @Transactional
  public List<Ville> getVillesByPopulationAndDepartement(String codeDepartement, int min, Integer max) throws VilleException {
    Departement dep = departementService.findByCodeDepartement(codeDepartement);
    return findByDepartement(dep)
      .stream()
      .filter(v -> v.getPopulation() >= min && (max == null || v.getPopulation() <= max))
      .collect(Collectors.toList());
  }

  /** Recherche toutes les villes d'un département triées par population décroissante */
  public List<Ville> findByDepartementOrderByPopulationDesc(Departement dep) {
    return villeDao.findAll()
      .stream()
      .filter(v -> v.getDepartement().equals(dep))
      .sorted((v1, v2) -> Integer.compare(v2.getPopulation(), v1.getPopulation()))
      .collect(Collectors.toList());
  }

  /** Recherche toutes les villes d'un département (sans tri) */
  public List<Ville> findByDepartement(Departement dep) {
    return villeDao.findAll()
      .stream()
      .filter(v -> v.getDepartement().equals(dep))
      .collect(Collectors.toList());
  }

  /**
   * cherche les villes concernés par l'export
   * @param min : seuil minimum de population
   * @return les villes concernés
   * @throws VilleException
   */
  public StringBuilder findForExport(int min) throws VilleException {
    List<VilleDto> villes = getVillesBetweenMinAndMax(min,null).stream().map(villeMapper::toDto).toList();

    if (villes.isEmpty()) {
      throw new VilleException("Aucune ville n’a une population superieur à " + min);
    }

    StringBuilder csv = new StringBuilder();

    for (VilleDto ville : villes){

      Departement dep = departementService.findByCodeDepartement(ville.getCodeDepartement());

      csv.append(ville.getNom()).append(";").append(ville.getPopulation()).append(";")
        .append(ville.getCodeDepartement()).append(";").append(dep.getNom());

    }


    return csv;
  }



  /**
   * return villes par page
   * @param pageable : page séléctionnée
   * @return toutes les villes de la page
   */
  public Page<Ville> findAll(Pageable pageable) {
    return villeRepository.findAll(pageable);
  }

  /**
   * retourne toutes les villes qui commence par une chaine de caracteres
   * @param nom : chaine de caracteres
   * @return une liste de villes
   */
  public List<Ville> findByNomDebut(String nom) {
    return villeRepository.findByNomStartingWithIgnoreCase(nom);
  }

  /**
   *
   * @param min
   * @return
   */
  public List<Ville> findByPopulationMin(int min) {
    return villeRepository.findByPopulationGreaterThanOrderByPopulationDesc(min);
  }

  /**
   *
   * @param min
   * @param max
   * @return
   */
  public List<Ville> findByPopulationMinMax(int min, int max) {
    return villeRepository.findByPopulationBetweenOrderByPopulationDesc(min, max);
  }

  /**
   *
   * @param idDep
   * @param min
   * @return
   * @throws VilleException
   */
  public List<Ville> findByDepartementMin(int idDep, int min) throws VilleException {
    Departement dep = departementService.findDepartementById(idDep);
    return villeRepository
      .findByDepartementAndPopulationGreaterThanOrderByPopulationDesc(dep, min);
  }

  /**
   *
   * @param idDep
   * @param min
   * @param max
   * @return
   * @throws VilleException
   */
  public List<Ville> findByDepartementMinMax(int idDep, int min, int max)
    throws VilleException {

    Departement dep = departementService.findDepartementById(idDep);
    return villeRepository
      .findByDepartementAndPopulationBetweenOrderByPopulationDesc(dep, min, max);
  }

  /**
   *
   * @param idDep
   * @param n
   * @return
   * @throws VilleException
   */
  public List<Ville> findTopNVilles(int idDep, int n) throws VilleException {
    Departement dep = departementService.findDepartementById(idDep);
    Pageable pageable = PageRequest.of(0, n);
    return villeRepository.findByDepartementOrderByPopulationDesc(dep, pageable);
  }


}
