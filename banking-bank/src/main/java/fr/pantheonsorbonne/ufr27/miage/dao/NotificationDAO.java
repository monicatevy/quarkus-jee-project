package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.exception.NotificationNotFoundException;
import fr.pantheonsorbonne.ufr27.miage.model.Notification;

import java.sql.Date;
import java.util.Collection;

public interface NotificationDAO {

    Notification findById(int idNotification);
    Collection<Notification> findNotificationAuthorisationAvailableForAccount(int idAccount) throws NotificationNotFoundException;
    Notification updateNotificationEtat(Notification notif, byte etat);
    Notification createNewNotification(String texte, byte etat, int idAccount, Date date, String type);
}
