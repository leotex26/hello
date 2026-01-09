package fr.diginamic.hello.services;

import fr.diginamic.hello.model.Region;
import fr.diginamic.hello.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegionService {

  @Autowired
  private final RegionRepository regionRepository;

  public RegionService(RegionRepository regionRepository) {
    this.regionRepository = regionRepository;
  }


  public Region save(Region region) {
    return regionRepository.save(region);
  }


  public List<Region> findAll() {
    return regionRepository.findAll();
  }


  public Optional<Region> findByCode(String code) {
    return regionRepository.findById(code);
  }


  public void deleteByCode(String code) {
    regionRepository.deleteById(code);
  }
}
