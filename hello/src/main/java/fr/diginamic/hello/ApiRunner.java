package fr.diginamic.hello;

import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.*;
import fr.diginamic.hello.services.DepartementService;
import fr.diginamic.hello.services.PaysService;
import fr.diginamic.hello.services.RegionService;
import fr.diginamic.hello.services.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Component
public class ApiRunner implements CommandLineRunner {

  private final RestTemplate restTemplate;

  @Autowired
  private PaysService paysService;

  @Autowired
  private RegionService regionService;

  @Autowired
  private DepartementService departementService;

  @Autowired
  private VilleService villeService;

  public ApiRunner(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public void run(String... args) throws VilleException {

    Pays france = new Pays("France");
    paysService.insert(france);

    // REGIONS
    RegionDto[] regionsDto =
      restTemplate.getForObject("https://geo.api.gouv.fr/regions", RegionDto[].class);

    for (RegionDto r : regionsDto) {
      Region region = new Region(r.getCode(), r.getNom());
      region.setPays(france);
      regionService.save(region);
    }

    // DEPARTEMENTS
    DepartementDto[] depsDto =
      restTemplate.getForObject("https://geo.api.gouv.fr/departements", DepartementDto[].class);

    for (DepartementDto d : depsDto) {
      if (d.getCodeRegion() == null) continue;

      departementService.create(
        d.getCode(),
        d.getNom(),
        d.getCodeRegion()
      );
    }

    // VILLES
    List<Departement> departements = departementService.findAll();

    for (Departement dep : departements) {

      String url = "https://geo.api.gouv.fr/departements/"
        + dep.getCode()
        + "/communes?fields=nom,code,codeDepartement,population";

      VilleDto[] villes = restTemplate.getForObject(url, VilleDto[].class);
      if (villes == null) continue;

      for (VilleDto v : villes) {
        if (v.getPopulation() <= 10_000) continue;

        villeService.save(
          new Ville(v.getNom(), v.getPopulation(), dep)
        );
      }
    }

    System.out.println("Import terminé !");
  }

}
