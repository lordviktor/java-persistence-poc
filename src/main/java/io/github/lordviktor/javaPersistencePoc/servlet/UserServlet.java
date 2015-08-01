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
        
        EntityManager entityManager = null;
        try{
            EntityManagerFactory entityManagerFactory = (EntityManagerFactory) getServletContext().getAttribute("emf");
            entityManager = entityManagerFactory.createEntityManager();
            
            //http://docs.oracle.com/javaee/6/tutorial/doc/gjrij.html
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
            Root<User> userRoot = criteria.from(User.class);
            criteria.select(userRoot);
            TypedQuery<User> query = entityManager.createQuery(criteria);
            List<User> users = query.getResultList();
        } finally {
            if(entityManager == null) {
                return;
            }
            // Close the database connection:
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            entityManager.close();
        }
    }
}
