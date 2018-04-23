package springboot.configapp;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * L3X1 FACIAL RECOGNITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on video)
 * <p>
 * PACKAGE configapp
 * <p>
 * Définit les méthodes de rappel pour personnaliser la configuration
 * Java pour Spring MVC activé via @EnableWebMvc.
 */
@Configuration
@EnableWebMvc
public class AppConfig implements WebMvcConfigurer {

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(10000000);
        return new CommonsMultipartResolver();
    }


}
