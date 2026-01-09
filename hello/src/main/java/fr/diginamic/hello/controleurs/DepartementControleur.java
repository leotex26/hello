package fr.diginamic.hello.controleurs;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import fr.diginamic.hello.exceptions.VilleException;
import fr.diginamic.hello.model.Departement;
import fr.diginamic.hello.model.DepartementDto;
import fr.diginamic.hello.model.Ville;
import fr.diginamic.hello.model.VilleDto;
import fr.diginamic.hello.model.mapper.DepartementMapper;
import fr.diginamic.hello.model.mapper.VilleMapper;
import fr.diginamic.hello.services.IDepartementService;
import fr.diginamic.hello.services.IVilleService;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * routes api pour le CRUD des départements
 */
@RestController
@RequestMapping("/departements")
public class DepartementControleur implements IDepartementControleur{

  @Autowired
  private IDepartementService departementService;

  @Autowired
  private IVilleService villeService;

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

    Departement dep = departementService.create(dto.getCode(), dto.getNom(), dto.getCodeRegion());
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

  @GetMapping(value = "/{code}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> exportDepartementPdf(@PathVariable String code) throws VilleException, DocumentException {

    Departement departement = departementService.findByCodeDepartement(code);
    List<Ville> villes = villeService.findByDepartement(departement);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Document document = new Document();
    PdfWriter.getInstance(document, out);
    document.open();

    Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

    document.add(new Paragraph("Département", titleFont));
    document.add(new Paragraph("Code : " + code));
    document.add(new Paragraph("Nom : " + departement.getNom()));
    document.add(new Paragraph(" "));

    document.add(new Paragraph(
      "Liste des villes du département :",
      boldFont
    ));
    document.add(new Paragraph(" "));

    PdfPTable table = new PdfPTable(2);
    table.addCell("Nom");
    table.addCell("Population");

    for (Ville v : villes) {
      table.addCell(v.getNom());
      table.addCell(String.valueOf(v.getPopulation()));
    }

    document.add(table);
    document.close();


    byte[] pdfBytes = out.toByteArray();

    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=departement_<code>.pdf")
      .contentType(MediaType.APPLICATION_PDF)
      .body(pdfBytes);

  }



}

