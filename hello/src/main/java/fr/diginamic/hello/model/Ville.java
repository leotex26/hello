package fr.diginamic.hello.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
public class Ville {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @Size(min = 2)
  private String nom;

  @Min(1)
  private int population;

  @ManyToOne
  private Departement departement;


  public Ville(String nom, int population, Departement departement) {
    this.nom = nom;
    this.population = population;
    this.departement = departement;
    departement.addVille(this);
  }

  public Ville() {
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



  public Integer getId() {
    return id;
  }

  public Departement getDepartement() {
    return departement;
  }

  public void setDepartement(Departement departement) {
    this.departement = departement;
  }

  @Override
  public String toString() {
    return "Ville{" +
      "id=" + id +
      ", nom='" + nom + '\'' +
      ", population=" + population +
      '}';
  }
}
