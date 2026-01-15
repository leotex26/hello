package fr.diginamic.hello.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Role implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToMany(mappedBy = "roles")
  private List<Utilisateur> users = new ArrayList<>();

  public Role(String name) {
    this.name = name;
  }

  public Role() {

  }

  @Override
  public String getAuthority() {
    return name;
  }

}
