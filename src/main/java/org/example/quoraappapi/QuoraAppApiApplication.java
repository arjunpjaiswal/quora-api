package org.example.quoraappapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class QuoraAppApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuoraAppApiApplication.class, args);
    }

}
