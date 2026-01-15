package fr.diginamic.hello.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Utilisateur implements UserDetails {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "utilisateur_role",
    joinColumns = @JoinColumn(name = "utilisateur_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private List<Role> roles = new ArrayList<>();

  public Utilisateur(String username, String password, Role role) {
    this.username = username;
    this.password = password;
    this.roles.add(role);
  }

  public Utilisateur(String username, String password, Role role, Role role2) {
    this.username = username;
    this.password = password;
    this.roles.add(role);
    this.roles.add(role2);
  }

  public Utilisateur() {

  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }
  @Override
  public String getPassword() {  return password;  }




  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String getUsername() {  return username;  }
  @Override
  public boolean isAccountNonExpired() { return true; }
  @Override
  public boolean isAccountNonLocked() { return true; }
  @Override
  public boolean isCredentialsNonExpired() { return true; }
  @Override
  public boolean isEnabled() { return true; }
}

