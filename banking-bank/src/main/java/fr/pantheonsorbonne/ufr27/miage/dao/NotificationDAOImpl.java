package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.exception.NotificationNotFoundException;
import fr.pantheonsorbonne.ufr27.miage.model.Notification;
import fr.pantheonsorbonne.ufr27.miage.service.Constante;
import fr.pantheonsorbonne.ufr27.miage.service.NotificationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.sql.Date;
import java.util.Collection;

@ApplicationScoped
public class NotificationDAOImpl implements NotificationDAO {

    @PersistenceContext(name = "mysql")
    EntityManager em;
    @Override
    @Transactional
    public Collection<Notification> findNotificationAuthorisationAvailableForAccount(int idAccount) throws NotificationNotFoundException {
        try{
            Collection<Notification> notifForAnAccountList = (Collection<Notification>) em.createQuery("Select n from Notification n where n.idAccount=:idAccount and n.type =:type and n.etat=:etat")
                    .setParameter("idAccount", idAccount)
                    .setParameter("type", Constante.AUTHORIZATION)
                    .setParameter("etat", Constante.VALID)
                    .getResultList();
            return notifForAnAccountList;
        } catch (NoResultException e){
            throw new NotificationNotFoundException();
        }
    }
    @Override
    @Transactional
    public Notification findById(int idNotification){return em.find(Notification.class, idNotification);};
    @Override
    @Transactional
    public Notification updateNotificationEtat(Notification notif, byte etat){
        try{
            if(notif != null){
                notif.setEtat(etat);
                em.merge(notif);
            }
            return notif;
        }catch(NoResultException e){
            return null;
        }
    }
    @Override
    @Transactional
    public Notification createNewNotification(String texte, byte etat, int idAccount, Date date, String type){
        Notification n = new Notification(texte,etat,idAccount,date,type);
        em.persist(n);
        return n;
    }
}
