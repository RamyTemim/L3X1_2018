package springboot.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * L3X1 FACIAL RECOGNITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on video)
 * <p>
 * PACKAGE application
 * <p>
 * Cette classe contient le main de l'application qui va permettre de demarrer l'application SpringBoot
 */
@SpringBootApplication
@ComponentScan(basePackages = "springboot")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}

