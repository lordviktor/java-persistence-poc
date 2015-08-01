package io.github.lordviktor.javaPersistencePoc.servlet.dataAccessLayer;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Referencia
 * 
 * http://www.objectdb.com/tutorial/jpa/eclipse/web/listener
 * 
 * @author victor
 *
 */
public class JPAContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("javaPersistencePocPU");
        servletContextEvent.getServletContext().setAttribute("emf", emf);
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        EntityManagerFactory emf = (EntityManagerFactory) servletContext.getAttribute("emf");
        emf.close();
    }

}
