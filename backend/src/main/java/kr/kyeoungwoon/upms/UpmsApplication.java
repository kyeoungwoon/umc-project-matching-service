package kr.kyeoungwoon.upms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UpmsApplication {

  public static void main(String[] args) {
    SpringApplication.run(UpmsApplication.class, args);
  }

}
