package fr.diginamic.hello.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public class VilleDto {

  /**
   * L'identifiant peut etre null à l'entrée du Back, l'id sera fournit en sorti (du coup il n'est pas en notNull)
   */
  private Integer id;

  @NotNull
  @Size(min = 2)
  private String nom;

  @Min(1)
  private int population;

  @NotNull
  @Size(min = 1, max = 3)
  private String codeDepartement;

  private Integer idDepartement;


  public VilleDto() {
  }

  public VilleDto(Integer id, String nom, int population, String codeDepartement,Integer idDepartement) {
    this.id = id;
    this.nom = nom;
    this.population = population;
    this.codeDepartement = codeDepartement;
    this.idDepartement = idDepartement;
  }

  // getters / setters

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public int getPopulation() {
    return population;
  }

  public void setPopulation(int population) {
    this.population = population;
  }

  public String getCodeDepartement() {
    return codeDepartement;
  }

  public void setCodeDepartement(String codeDepartement) {
    this.codeDepartement = codeDepartement;
  }

  public Integer getIdDepartement() {
    return idDepartement;
  }

  public void setIdDepartement(Integer idDepartement) {
    this.idDepartement = idDepartement;
  }
}

