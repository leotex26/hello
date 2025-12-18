package fr.diginamic.hello.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


  @ExceptionHandler(VilleException.class)
  public ResponseEntity<String> handleVilleException(VilleException ex) {
    System.out.println("VilleException : " + ex.getMessage());
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body("Erreur ville : " + ex.getMessage());
  }



}
