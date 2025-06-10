package io.github.adil_jr.literadyu;

import io.github.adil_jr.literadyu.main.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteradyuApplication implements CommandLineRunner {

    @Autowired
    private Main main;

    public static void main(String[] args) {
        SpringApplication.run(LiteradyuApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        main.showMenu();
    }
}