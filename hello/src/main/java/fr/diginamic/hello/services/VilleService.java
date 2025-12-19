package fr.diginamic.hello.services;

import fr.diginamic.hello.Validator.VilleValidator;
import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Ville;
import fr.diginamic.hello.repositories.VilleDao;
import fr.diginamic.hello.repositories.VilleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VilleService {

  @Autowired
  VilleValidator villeValidator;

  @Autowired
  DepartementService departementService;

  @Autowired
  VilleDao villeDao;

  @Autowired
  VilleRepository villeRepository;
  @Autowired
  private PaysService paysService;


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
  public Ville creerVille(String nom, int population,String codeDepartement, Integer idDepartement) throws VilleException {

    Departement departement =departementService.findOrCreate(codeDepartement, idDepartement);

    Ville ville = new Ville(nom, population, departement);

    Errors errors = new BeanPropertyBindingResult(ville, "ville");
    villeValidator.validate(ville, errors);
    if (errors.hasErrors()) {
      throw new VilleException("Attributs de ville invalides");
    }

    return villeRepository.save(ville);
  }

  @Transactional
  public Ville modifierVille(Integer id, Ville villeModifiee) throws VilleException {
    Ville villeExistante = findById(id);
    if (villeExistante == null) {
      throw new VilleException("Ville inexistante avec id " + id);
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
}
