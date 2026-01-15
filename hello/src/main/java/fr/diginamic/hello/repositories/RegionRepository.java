package fr.diginamic.hello.repositories;

import fr.diginamic.hello.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RegionRepository extends JpaRepository<Region,String> {

}
