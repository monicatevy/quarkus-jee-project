package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.exception.NotificationNotFoundException;
import fr.pantheonsorbonne.ufr27.miage.model.Notification;
import fr.pantheonsorbonne.ufr27.miage.model.Operation;
import fr.pantheonsorbonne.ufr27.miage.service.Constante;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Collection;

@ApplicationScoped
public class OperationDAOImpl implements OperationDAO {

    @Inject
    EntityManager em;

    @Override
    public Collection<Operation> getOperationsForAccount(int idAccount) {

        Collection<Operation> op = (Collection<Operation>) em.createQuery("Select o from Operation o where o.idAccount=:idAccount")
                .setParameter("idAccount", idAccount)
                .getResultList();
        return op;
    }


}
