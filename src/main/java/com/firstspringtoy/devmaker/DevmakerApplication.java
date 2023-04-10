package com.firstspringtoy.devmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DevmakerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevmakerApplication.class, args);
    }

}
