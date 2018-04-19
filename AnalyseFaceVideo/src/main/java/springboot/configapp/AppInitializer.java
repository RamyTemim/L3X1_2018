package springboot.configapp;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * L3X1 FACIAL RECOGNITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on video)
 * <p>
 * PACKAGE configapp
 * <p>
 * Cette classe qui implémente l'interface WebApplicationInitializer est utiliser
 * pour configurer l'envirenement Servlet Contexte elle remplace le fichier Web.xml
 */
public class AppInitializer implements WebApplicationInitializer {


    /**
     * Configurez les données Servlet Context avec les servlets, les filtres,
     * les auditeurs contexte params et les attributs nécessaires à l' initialisation d'application web.
     * @param servletContext contexte des servelte
     */
    @Override
    public void onStartup(ServletContext servletContext) {
        // Créer le contexte d'application Spring
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);
        rootContext.setServletContext(servletContext);
        // Gérer le cycle de vie du contexte d'application racine
        servletContext.addListener(new ContextLoaderListener(rootContext));
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",
                new DispatcherServlet(rootContext));
        dispatcher.addMapping("/");
        dispatcher.setLoadOnStartup(1);
        FilterRegistration.Dynamic corsFilter = servletContext.addFilter("corsFiler", CROSFiler.class);
        corsFilter.addMappingForUrlPatterns(null, false, "/*");
    }
}
