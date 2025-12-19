package fr.diginamic.hello.model.mapper;

import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.Ville;
import fr.diginamic.hello.model.VilleDto;
import fr.diginamic.hello.services.DepartementService;
import fr.diginamic.hello.services.PaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * classes pour mapper entre le modele et les dto
 */
@Component
public class VilleMapper {

  @Autowired
  DepartementService departementService;
  @Autowired
  private PaysService paysService;

  /**
   * methode pour transformer une ville en villeDto
   * @param ville
   * @return
   */
  public VilleDto toDto(Ville ville) {

    if (ville == null) {
      return null;
    }

    VilleDto dto = new VilleDto();
    dto.setId(ville.getId());
    dto.setNom(ville.getNom());
    dto.setPopulation(ville.getPopulation());

    if (ville.getDepartement() != null) {
      dto.setCodeDepartement(ville.getDepartement().getCode());
      dto.setIdDepartement(ville.getDepartement().getId());
    }


    return dto;
  }


  /**
   * methode pour transformer une villeDto en ville
   * @param villeDto
   * @return
   * @throws VilleException
   */
  public Ville toBean(VilleDto villeDto) throws VilleException {

    if (villeDto == null) {
      return null;
    }

    Ville ville = new Ville();
    ville.setNom(villeDto.getNom());
    ville.setPopulation(villeDto.getPopulation());

    if (villeDto.getCodeDepartement() == null && villeDto.getIdDepartement() == null ) {
      throw new VilleException("le code du département ou id de departement n'est pas renseigné !");
    }

    Departement departement = departementService.findOrCreate(villeDto.getCodeDepartement(), villeDto.getIdDepartement());
    if (departement == null) {
      throw new VilleException("Departement non reconnu");
    }else{
      ville.setDepartement(departement);
    }

    return ville;
  }


}
