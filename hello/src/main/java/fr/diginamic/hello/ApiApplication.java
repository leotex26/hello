package fr.diginamic.hello;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;


@SpringBootApplication
@Profile("!test")
public class ApiApplication {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(ApiApplication.class);
    app.setWebApplicationType(WebApplicationType.NONE);
    app.run(args);
  }
}


