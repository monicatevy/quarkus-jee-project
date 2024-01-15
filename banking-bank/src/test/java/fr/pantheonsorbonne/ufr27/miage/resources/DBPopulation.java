package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class DBPopulation {
    public EntityManager getEm() {
        return em;
    }

    @Inject
    EntityManager em;


    @Transactional
    public void truncateAllTables() {
        // Disable referential integrity for H2
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        // Query to find all tables
        List<String> tableNames = em.createNativeQuery(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES " +
                        "WHERE TABLE_SCHEMA='PUBLIC'").getResultList();

        // Truncate each table
        for (String tableName : tableNames) {
            em.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        }

        // Re-enable referential integrity for H2
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
    @Transactional
    public TestData createMaterial() {

        Operation op = new Operation();
        op.setIdAccount(1);
        op.setIdTransaction(1);
        op.setIdOperation(1);
        em.persist(op);

        Operation op2 = new Operation();
        op.setIdAccount(1);
        op.setIdTransaction(2);
        op.setIdOperation(2);
        em.persist(op);


        return new TestData(op, op2);

    }


}