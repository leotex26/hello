package fr.diginamic.hello.services;

import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Pays;
import fr.diginamic.hello.repositories.DepartementRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartementService {

  @Autowired
  private DepartementRepository departementRepository;

  @Autowired
  private PaysService paysService;

  /* ================== CRUD ================== */

  @Transactional
  public Departement create(String code, String nom) throws VilleException {
    Pays pays = paysService.findPays(1);
    Departement dep = new Departement(code, nom, pays);
    return departementRepository.save(dep);
  }

  @Transactional
  public Departement update(int id, String code, String nom) throws VilleException {
    Departement dep = findDepartementById(id);
    dep.setCode(code);
    dep.setNom(nom);
    return departementRepository.save(dep);
  }

  public void delete(int id) throws VilleException {
    Departement dep = findDepartementById(id);
    departementRepository.delete(dep);
  }

  /* ================== FIND ================== */

  public List<Departement> findAll() {
    return departementRepository.findAll();
  }

  public Departement findDepartementById(int id) throws VilleException {
    return departementRepository.findById(id)
      .orElseThrow(() -> new VilleException("Département non trouvé"));
  }

  public Departement findByCodeDepartement(String code) throws VilleException {
    List<Departement> deps = departementRepository.findAll().stream()
      .filter(d -> d.getCode().equals(code))
      .toList();

    if (deps.isEmpty()) {
      throw new VilleException("Aucun département trouvé avec le code : " + code);
    }
    if (deps.size() > 1) {
      throw new VilleException("Doublon de départements pour le code : " + code);
    }
    return deps.get(0);
  }

  @Transactional
  public Departement findOrCreate(String codeDepartement, Integer idDepartement) throws VilleException {


    if (idDepartement != null) {
      Departement dep = departementRepository.findById(idDepartement)
        .orElseThrow(() ->
          new VilleException("Aucun département trouvé avec l'id : " + idDepartement)
        );

      // Sécurité : cohérence code / id
      if (codeDepartement != null && !codeDepartement.equals(dep.getCode())) {
        throw new VilleException(
          "Incohérence entre idDepartement et codeDepartement"
        );
      }

      return dep;
    }


    if (codeDepartement != null) {
      List<Departement> departements = departementRepository.findAll().stream()
        .filter(d -> d.getCode().equals(codeDepartement))
        .toList();

      if (departements.size() > 1) {
        throw new VilleException("Doublon de départements pour le code : " + codeDepartement);
      }

      if (!departements.isEmpty()) {
        return departements.get(0);
      }


      return create(codeDepartement, null);
    }


    throw new VilleException("codeDepartement ou idDepartement requis");
  }

}
