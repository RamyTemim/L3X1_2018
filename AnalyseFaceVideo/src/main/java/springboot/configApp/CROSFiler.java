package springboot.configApp;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CROSFiler  implements  Filter{
    @Override
    public void init(FilterConfig filterConfig)  {
        //implémentation de la methode abstraite
    }

    @Override
    public void doFilter(ServletRequest req , ServletResponse res , FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = ( HttpServletResponse ) res;
        response.setHeader( "Access-Control-Allow-Origin", "*" );
        response.setHeader( "Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE" );
        response.setHeader( "Access-Control-Max-Age", "3600" );
        response.setHeader( "Access-Control-Allow-Headers", "x-requested-with,Content-type" );
        chain.doFilter( req, res );
    }

    @Override
    public void destroy() {
//implémentation de la methode abstraite
    }
}
