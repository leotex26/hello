package fr.diginamic.hello.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Region {

  @Id
  private String code;

  private String nom;


  @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Departement> departements = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "pays_id")
  private Pays pays;



  // Constructeurs
  public Region() {}

  public Region(String code, String nom) {
    this.code = code;
    this.nom = nom;
  }

  // Getters & setters
  public String getCode() { return code; }
  public void setCode(String code) { this.code = code; }

  public String getNom() { return nom; }
  public void setNom(String nom) { this.nom = nom; }

  public List<Departement> getDepartements() { return departements; }
  public void setDepartements(List<Departement> departements) { this.departements = departements; }

  public void addDepartement(Departement departement){
    departements.add(departement);
  }

  public void setPays(Pays pays) {
    this.pays = pays;
  }
}

