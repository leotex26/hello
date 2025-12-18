package fr.diginamic.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class RecensementApplication {

  @GetMapping("/")
  public String test() {
    return "OK";
  }

	public static void main(String[] args) {
		SpringApplication.run(RecensementApplication.class, args);
	}

}
