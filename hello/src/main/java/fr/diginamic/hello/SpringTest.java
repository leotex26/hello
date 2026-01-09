package fr.diginamic.hello;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * ne lance qu'une seule classe de test : VilleControllerTest
 */
@EnableTransactionManagement
@SpringBootApplication
@Profile("test")
public class SpringTest {
  public static void main(String[] args) {
    System.setProperty("spring.profiles.active", "test");
    SpringApplication.run(SpringTest.class, args);
  }
}

