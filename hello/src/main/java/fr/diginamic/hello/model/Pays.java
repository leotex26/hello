package fr.diginamic.hello.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Pays {

  public Pays() {

  }


  public Pays(String nom, Set<Departement> departements) {
    this.nom = nom;
    this.departements = departements;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String nom;

  @OneToMany(mappedBy = "pays")
  private Set<Departement> departements;


  public Pays(String nom) {
    this.nom = nom;
    departements = new HashSet<Departement>();
  }



  public String getNom() {
    return nom;
  }

  public Pays addDepartement(Departement departement) {
    departements.add(departement);
    departement.setPays(this);
    return this;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
