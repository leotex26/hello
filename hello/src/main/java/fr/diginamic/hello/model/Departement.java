package fr.diginamic.hello.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
public class Departement {

  public Departement() {

  }

  public Departement(String nom, String code, Pays pays) {
    this.nom = nom;
    this.code = code;
    this.pays = pays;
    pays.addDepartement(this);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String nom;
  private String code;

  @OneToMany(mappedBy = "departement")
  @JsonIgnore
  private Set<Ville> villes = new HashSet<>();

  @ManyToOne
  private Pays pays;




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

  public Set<Ville> getVilles() {
    return villes;
  }

  public void addVille(Ville ville) {
    villes.add(ville);
  }

  public void removeVille(Ville ville) {
    villes.remove(ville);
  }

  public Pays getPays() {
    return pays;
  }

  public void setPays(Pays pays) {
    this.pays = pays;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
