package io.github.lordviktor.javaPersistencePoc.servlet.dataAccessLayer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateManualSessionFactoryBuilder implements ServletContextListener {

    /**
     * Assim configuramos o session factory que utilizamos para acessar sessions e dair efetuar
     * consultas :) Essa é uma opcao pragramatica, aonde podemos utilizar constants e class para
     * definir propriedades. Também utilizo a propriedade "packageToScan" para configurar em qual
     * pacote o hibernate deve pesquisar classes com anotacoes.
     * 
     * O link abaixo serve para acharmos as configs que podemos usar
     * http://docs.jboss.org/hibernate/orm/4.1/javadocs/constant-values.html
     */
    public HibernateManualSessionFactoryBuilder() {

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        SessionFactory sessionFactory = (SessionFactory) servletContext.getAttribute("programaticallySessionFactory");
        sessionFactory.close();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        SessionFactory sessionFactory = buildSessionFactory();
        servletContextEvent.getServletContext().setAttribute("programaticallySessionFactory", sessionFactory);
    }

    @SuppressWarnings("deprecation")
    private SessionFactory buildSessionFactory() {
        // Daonde surgiu o entity manager factory da especificação JPA
        SessionFactory sessionFactory = null;

        // Na minha época funcionava, agora é deprecated, a partir do surgimento do JPA
        // ainda nao seu usar o novo metodo buildSessionFactory usando ServiceRegistry
        sessionFactory = new Configuration()
            .configure("/hibernate/hibernate.cfg.xml")
            .buildSessionFactory();

        // ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
        // .applySettings(properties)
        // .buildServiceRegistry();
        return sessionFactory;
    }
}
