package fr.diginamic.hello.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public class SignedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String userMaj;

  private LocalDateTime dateMaj;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

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


}
