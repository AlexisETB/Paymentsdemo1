package ec.edu.uce.paymentsdemo.jpa.Manager;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.EntityTransaction;
import java.util.logging.Logger;

@ApplicationScoped
public class EntityManagerProvider {
    private static final String PERSISTENCE_UNIT_NAME = "paymentUnit";

    @Inject
    private EntityManagerFactory emf;

    private Logger log;

    @PostConstruct
    public void init() {
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception e) {
            System.err.println("Error initializing EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize EntityManagerFactory", e);
        }
    }

    public  EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @PreDestroy
    public void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            log.info("EntityManager closed");
        }
    }
}
