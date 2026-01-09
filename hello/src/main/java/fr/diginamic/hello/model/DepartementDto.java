package fr.diginamic.hello.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class DepartementDto {

  private Integer id;

  @NotBlank
  @Size(min = 2)
  private String code;

  private String nom;

  private String codeRegion;

  public DepartementDto() {
  }

  public DepartementDto(Integer id, String code, String nom, String codeRegion) {
    this.id = id;
    this.code = code;
    this.nom = nom;
    this.codeRegion = codeRegion;
  }

  // getters / setters

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }


  public String getCodeRegion() { return codeRegion; }
  public void setCodeRegion(String codeRegion) { this.codeRegion = codeRegion; }
}
