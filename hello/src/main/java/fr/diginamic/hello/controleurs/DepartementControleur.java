package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.DepartementDto;
import fr.diginamic.hello.model.Ville;
import fr.diginamic.hello.model.VilleDto;
import fr.diginamic.hello.model.mapper.DepartementMapper;
import fr.diginamic.hello.model.mapper.VilleMapper;
import fr.diginamic.hello.services.DepartementService;
import fr.diginamic.hello.services.VilleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departements")
public class DepartementControleur {

  @Autowired
  private DepartementService departementService;

  @Autowired
  private VilleService villeService;

  @Autowired
  private DepartementMapper departementMapper;

  @Autowired
  private VilleMapper villeMapper;

  /* ================== CRUD ================== */

  @GetMapping
  public List<DepartementDto> findAll() {
    return departementService.findAll().stream()
      .map(departementMapper::toDto)
      .toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<DepartementDto> findById(@PathVariable int id) throws VilleException {
    Departement dep = departementService.findDepartementById(id);
    return ResponseEntity.ok(departementMapper.toDto(dep));
  }

  @PostMapping
  public ResponseEntity<DepartementDto> create(
    @Valid @RequestBody DepartementDto dto
  ) throws VilleException {

    Departement dep = departementService.create(dto.getCode(), dto.getNom());
    return ResponseEntity.ok(departementMapper.toDto(dep));
  }

  @PutMapping("/{id}")
  public ResponseEntity<DepartementDto> update(
    @PathVariable int id,
    @Valid @RequestBody DepartementDto dto
  ) throws VilleException {

    Departement dep = departementService.update(id, dto.getCode(), dto.getNom());
    return ResponseEntity.ok(departementMapper.toDto(dep));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable int id) throws VilleException {
    departementService.delete(id);
    return ResponseEntity.ok("Département supprimé avec succès");
  }

  /* ================== MÉTIERS ================== */

  /**
   * Lister les n plus grandes villes d’un département
   */
  @GetMapping("/{id}/top-villes")
  public ResponseEntity<List<VilleDto>> topNVilles(
    @PathVariable("id") int id,
    @RequestParam int n
  ) throws VilleException {

    Departement dep = departementService.findDepartementById(id);

    List<VilleDto> villes = villeService
      .findByDepartementOrderByPopulationDesc(dep)
      .stream()
      .limit(n)
      .map(villeMapper::toDto)
      .toList();

    return ResponseEntity.ok(villes);
  }

  /**
   * Villes par population min/max
   */
  @GetMapping("/{id}/villes")
  public ResponseEntity<List<VilleDto>> villesParPopulation(
    @PathVariable int id,
    @RequestParam int min,
    @RequestParam(required = false) Integer max
  ) throws VilleException {

    Departement dep = departementService.findDepartementById(id);

    List<VilleDto> villes = villeService.findByDepartement(dep).stream()
      .filter(v -> v.getPopulation() >= min && (max == null || v.getPopulation() <= max))
      .map(villeMapper::toDto)
      .toList();

    return ResponseEntity.ok(villes);
  }
}

