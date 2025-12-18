package fr.diginamic.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class HelloApplication {

  @GetMapping("/")
  public String test() {
    return "OK";
  }

	public static void main(String[] args) {
		SpringApplication.run(HelloApplication.class, args);
	}

}
