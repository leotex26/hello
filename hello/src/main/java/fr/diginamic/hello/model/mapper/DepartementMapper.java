package fr.diginamic.hello.model.mapper;

import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.DepartementDto;
import org.springframework.stereotype.Component;

@Component
public class DepartementMapper {

  public DepartementDto toDto(Departement departement) {
    if (departement == null) {
      return null;
    }
    DepartementDto departementDto = new DepartementDto();
    departementDto.setId(departement.getId());
    departementDto.setCode(departement.getCode());
    departementDto.setNom(departement.getNom());
    return departementDto;
  }

  public Departement toBean(DepartementDto departementDto) {
    if (departementDto == null) {
      return null;
    }
    Departement departement = new Departement();
    departement.setCode(departementDto.getCode());
    departement.setNom(departementDto.getNom());
    return departement;
  }

}
