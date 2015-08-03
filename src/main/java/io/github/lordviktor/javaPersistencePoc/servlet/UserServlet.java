package io.github.lordviktor.javaPersistencePoc.servlet;

import io.github.lordviktor.javaPersistencePoc.entity.User;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 2047806092733379354L;

    private static final Logger LOG = LoggerFactory.getLogger(UserServlet.class);

    /**
     * curl -i http://localhost:8080/java-persistence-poc/api/servlet/user
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // super.doGet(req, resp);

        LOG.info("Servlet call on GET method. Listing users.");

        // usingJpaSpecification();

        usingProgramaticallyHibernate();
    }

    @SuppressWarnings("unchecked")
    private void usingProgramaticallyHibernate() {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction transaction = null;

        try {
            sessionFactory = (SessionFactory) getServletContext().getAttribute("programaticallySessionFactory");
            session = sessionFactory.getCurrentSession();
            transaction = session.getTransaction();
            transaction.begin();
            List<User> users = session.createCriteria(User.class).list();
        } catch (Exception ex) {
            LOG.error("Error while try to fetch users using programatically hibernate with this given cause {}", ex);
        } finally {
            if (transaction.isActive())
                transaction.rollback();
            if (session.isOpen())
                session.close();
        }
    }

    /**
     * Exemplo de um select * utilizando pura especificação JPA
     * 
     * Um artigo interessante
     * 
     * Why JPA (Oracle) https://docs.oracle.com/html/E24396_01/ejb3_overview_why.html
     * 
     * @param entityManagerFactory
     * @return
     */
    private void usingJpaSpecification() {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        // Precisaria tratar caso servlet context attr nao exista.
        entityManagerFactory = (EntityManagerFactory) getServletContext().getAttribute("emf");

        try {
            entityManager = entityManagerFactory.createEntityManager();

            // http://docs.oracle.com/javaee/6/tutorial/doc/gjrij.html
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
            Root<User> userRoot = criteria.from(User.class);
            criteria.select(userRoot);
            TypedQuery<User> query = entityManager.createQuery(criteria);
            List<User> users = query.getResultList();
        } catch (Exception ex) {
            LOG.error("Error while try to fetch users using JPA specification with this given cause {}", ex);
        } finally {
            // Close the database connection
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            entityManager.close();
        }
    }
}
