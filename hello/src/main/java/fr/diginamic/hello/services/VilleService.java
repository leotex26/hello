package fr.diginamic.hello.services;

import fr.diginamic.hello.Validator.VilleValidator;
import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Ville;
import fr.diginamic.hello.repositories.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VilleService {

  @Autowired
  VilleValidator villeValidator;

  @Autowired
  VilleRepository villeRepository;

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


  public Ville save(Ville ville) {
    return villeRepository.save(ville);
  }


  public Ville findById(int id) {
    return villeRepository.findById(id).orElse(null);
  }

  public List<Ville> findAll() {
    return villeRepository.findAll();
  }


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
    return villes.stream().filter(v -> v.getNom().startsWith(nom)).collect(Collectors.toList());
  }
}
