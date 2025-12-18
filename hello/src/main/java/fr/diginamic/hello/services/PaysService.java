package fr.diginamic.hello.services;

import fr.diginamic.hello.model.Pays;
import fr.diginamic.hello.repositories.PaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaysService {

  @Autowired
  private PaysRepository paysRepository;

  public Pays findOrCreate(String pays) {

    if (findPaysParNom(pays) != null) {
      return findPaysParNom(pays);
    }else{
      Pays newPays = new Pays(pays);
      paysRepository.save(newPays);
      return newPays;
    }

  }

  public Pays findPays(int id) {
    return paysRepository.findById(id).orElse(null);
  }

  public Pays findPaysParNom(String pays) {
    return findAll().stream().filter(p -> p.getNom().equals(pays)).findFirst().orElse(null);
  }

  public List<Pays> findAll() {
    return paysRepository.findAll();
  }

}
