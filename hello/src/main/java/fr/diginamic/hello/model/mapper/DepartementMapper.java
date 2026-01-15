package fr.diginamic.hello.model.mapper;

import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.DepartementDto;
import fr.diginamic.hello.services.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepartementMapper {

  @Autowired
  RegionService regionService;

  public DepartementDto toDto(Departement departement) {
    if (departement == null) {
      return null;
    }
    DepartementDto departementDto = new DepartementDto();
    departementDto.setId(departement.getId());
    departementDto.setCode(departement.getCode());
    departementDto.setNom(departement.getNom());
    departementDto.setCodeRegion(departement.getRegion().getCode());
    return departementDto;
  }

  public Departement toBean(DepartementDto departementDto) {
    if (departementDto == null) {
      return null;
    }
    Departement departement = new Departement();
    departement.setCode(departementDto.getCode());
    departement.setNom(departementDto.getNom());
    departement.setRegion(regionService.findByCode(departementDto.getCodeRegion()).get()); // pas top j'imagine
    return departement;
  }

}
