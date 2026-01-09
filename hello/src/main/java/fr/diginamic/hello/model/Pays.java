package fr.diginamic.hello.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Pays {

  public Pays() {

  }

  public Pays(String nom) {
    this.nom = nom;
    this.regions = new HashSet<>();
  }


  public Pays(String nom, Set<Region> regions) {
    this.nom = nom;
    this.regions = regions;
  }


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String nom;

  @OneToMany(mappedBy = "pays", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<Region> regions = new HashSet<>();






  public String getNom() {
    return nom;
  }

  public Pays addRegion(Region region) {
    regions.add(region);
    region.setPays(this);
    return this;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
