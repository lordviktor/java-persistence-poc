package io.github.lordviktor.javaPersistencePoc.servlet.dataAccessLayer;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.context.internal.ThreadLocalSessionContext;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.AvailableSettings;

public class HibernateProgramaticallySessionFactoryBuilder implements ServletContextListener {

    /**
     * Assim configuramos o session factory que utilizamos para acessar sessions e dair efetuar
     * consultas :) Essa é uma opcao pragramatica, aonde podemos utilizar constants e class para
     * definir propriedades. Também utilizo a propriedade "packageToScan" para configurar em qual
     * pacote o hibernate deve pesquisar classes com anotacoes.
     * 
     * O link abaixo serve para acharmos as configs que podemos usar
     * http://docs.jboss.org/hibernate/orm/4.1/javadocs/constant-values.html
     */
    public HibernateProgramaticallySessionFactoryBuilder() {

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

        Properties properties = new Properties();
        properties.put(Environment.DIALECT, H2Dialect.class.getName());
//        properties.put(Environment.DRIVER, "org.h2.Driver");// nao temos dependecia com esta
//                                                            // biblioteca pois ela é fornecida
//                                                            // direto no servidor no classpath do
//                                                            // container.
//        properties.put(Environment.URL, "jdbc:h2:mem:javapersistencepoc;DB_CLOSE_ON_EXIT=FALSE");
//        properties.put(Environment.USER, "sa");
//        properties.put(Environment.PASS, "");

        properties.put(Environment.DATASOURCE, "java:comp/env/jdbc/javaPersistecepocDS");
        
        properties.put(Environment.HBM2DDL_AUTO, "update");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(AvailableSettings.NAMING_STRATEGY, ImprovedNamingStrategy.class.getName());
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, ThreadLocalSessionContext.class.getName()); // session managed o ideal
        properties.put("packagesToScan", "io.github.lordviktor.javaPersistencePoc.entity");


        // Na minha época funcionava, agora é deprecated, a partir do surgimento do JPA
        // ainda nao seu usar o novo metodo buildSessionFactory usando ServiceRegistry
        sessionFactory = new Configuration().setProperties(properties).buildSessionFactory();

        // ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
        // .applySettings(properties)
        // .buildServiceRegistry();
        return sessionFactory;
    }
}
