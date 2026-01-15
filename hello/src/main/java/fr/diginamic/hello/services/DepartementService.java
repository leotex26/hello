package fr.diginamic.hello.services;

import fr.diginamic.hello.controleurs.DepartementControleur;
import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Pays;
import fr.diginamic.hello.model.Region;
import fr.diginamic.hello.repositories.DepartementRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartementService implements IDepartementService{

  @Autowired
  private DepartementRepository departementRepository;

  @Autowired
  private RegionService regionService;

  @Autowired
  private IPaysService paysService;

  private static final Logger LOGGER = LoggerFactory.getLogger(DepartementService.class);

  /* ================== CRUD ================== */


  public Departement create(String code, String nom) throws VilleException {
    Optional<Region> OptRegion = regionService.findByCode("11");
    Region region = OptRegion.get();

    Departement dep = new Departement(code, nom, region);
    return departementRepository.save(dep);
  }

  @Transactional
  public Departement create(String code, String nom, String codeRegion) throws VilleException {

    Region region = regionService.findByCode(codeRegion)
      .orElseThrow(() ->
        new VilleException("Aucune région trouvée pour le code : " + codeRegion)
      );


    Optional<Departement> existing = departementRepository.findByCode(code);
    if (existing.isPresent()) {
      return existing.get();
    }

    Departement dep = new Departement(code, nom, region);
    return departementRepository.save(dep);
  }

  @Transactional
  public Departement create(Departement dep) {

    Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();

    if (authentication.isAuthenticated()){
      dep.setDateMaj(LocalDateTime.now());
      dep.setUserMaj(authentication.getName());
      LOGGER.info("Departement "+ dep.getNom() +" mis à jour par :"+ authentication.getName());
    }else{
      dep.setDateMaj(LocalDateTime.now());
      dep.setUserMaj("Système");
      LOGGER.info("Departement "+ dep.getNom() +" mis à jour par : Système");
    }

    Optional<Departement> existing = departementRepository.findByCode(dep.getCode());
    if (existing.isPresent()) {
      return existing.get();
    }

    return departementRepository.save(dep);
  }


  @Transactional
  public Departement update(int id, String code, String nom) throws VilleException {
    Departement dep = findDepartementById(id);
    dep.setCode(code);
    dep.setNom(nom);
    return departementRepository.save(dep);
  }



  @Transactional
  public Departement update(Departement departement) throws VilleException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Departement dep = findDepartementById(departement.getId());

    if (authentication.isAuthenticated()) {
      dep.setDateMaj(LocalDateTime.now());
      dep.setUserMaj(authentication.getName());
      LOGGER.info("Departement "+ dep.getNom() +" mis à jour par :"+ authentication.getName());
    } else {
      dep.setDateMaj(LocalDateTime.now());
      dep.setUserMaj("Système");
      LOGGER.info("Departement "+ dep.getNom() +" mis à jour par : Système");
    }

    dep.setCode(departement.getCode());
    dep.setNom(departement.getNom());
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


      return create(codeDepartement, null, null);
    }


    throw new VilleException("codeDepartement ou idDepartement requis");
  }

}
