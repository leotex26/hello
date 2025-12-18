package fr.diginamic.hello.services;

import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Pays;
import fr.diginamic.hello.model.Ville;
import fr.diginamic.hello.repositories.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartementService {

  @Autowired
  private DepartementRepository departementRepository;


  public Departement findOrCreate(String code, String nom, Pays pays) throws VilleException {
    List<Departement> departements = findParNom(nom);

    if (departements.size() > 1) {
      throw new VilleException("doublon de départements");
    }

    if (departements.isEmpty()) {
      Departement d = new Departement(code, nom, pays);
      return departementRepository.save(d);
    }

    return departements.get(0);
  }


    public List<Departement> findAll() {
      return departementRepository.findAll();
    }

  public List<Departement> findParNom(String nom) {
    return departementRepository.findAll().stream().filter(d -> d.getNom().equals(nom)).collect(Collectors.toList());
  }


  public Departement findByCodeDepartement(String codeDepartement) throws VilleException {

    List<Departement> departements = findAll().stream()
      .filter(d -> d.getCode().equals(codeDepartement))
      .toList();

    if (departements.isEmpty()) {
      throw new VilleException("Département introuvable");
    }

    if (departements.size() > 1) {
      throw new VilleException("Doublon de départements");
    }

    return departements.get(0);
  }


}
