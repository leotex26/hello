package fr.diginamic.hello.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Departement {

  public Departement() {

  }

  public Departement(String code, String nom, Region region) {
    this.nom = nom;
    this.code = code;
    this.region = region;
    region.addDepartement(this);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;


  private String nom;

  @Column(unique = true, nullable = false)
  private String code;

  @OneToMany(mappedBy = "departement")
  @JsonIgnore
  private Set<Ville> villes = new HashSet<>();

  @ManyToOne
  private Region region;


  private String userMaj;

  private LocalDateTime dateMaj;


  public String getUserMaj() {
    return userMaj;
  }

  public void setUserMaj(String userMaj) {
    this.userMaj = userMaj;
  }

  public LocalDateTime getDateMaj() {
    return dateMaj;
  }

  public void setDateMaj(LocalDateTime dateMaj) {
    this.dateMaj = dateMaj;
  }

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


  public Region getRegion() {
    return region;
  }

  public void setRegion(Region region) {
    this.region = region;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
