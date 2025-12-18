package fr.diginamic.hello.Validator;

import fr.diginamic.hello.model.Ville;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class VilleValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Ville.class.equals(clazz);
  }


  @Override
  public void validate(Object target, Errors errors) {

    Ville ville = (Ville) target;

      if(ville.getNom() == null || ville.getNom().isEmpty()){
        errors.rejectValue("nom", "Ville nom n'est pas rentré");
      }

    if(ville.getPopulation() < 1){
      errors.rejectValue("population", "Ville population est sous le seuil minimum");
    }

  }


}
