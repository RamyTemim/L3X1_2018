package springboot.configapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * L3X1 FACIAL RECOGNITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on video)
 * <p>
 * PACKAGE controller
 * <p>
 * Filtres effectuer un filtrage . Chaque filtre a accès à un objet AppConfig
 * à partir duvquel il peut obtenir les paramètres d'initialisation, une référence
 * à la Servlet Context qu'il peut utiliser, par exemple, pour charger les ressources nécessaires
 * pour les tâches de filtrage.
 */
@Component
public class CROSFiler implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        /*implémentation de la methode abstraite init
           de l'interface Filter
        */
    }

    public static final Logger log = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Content-type");
        try {
            chain.doFilter(req, res);
        } catch (IOException | ServletException e) {
            log.info("Erreur dans la méthode doFilter : " + e);
        }
    }

    @Override
    public void destroy() {
     /*implémentation de la methode abstraite
       destroy de l'interface Filter
      */
    }
}
