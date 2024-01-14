package top.nextnet.dao;


import fr.pantheonsorbonne.ufr27.miage.model.Notification;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import top.nextnet.model.Token;

import java.sql.Date;

@ApplicationScoped
public class TokenDAOimpl implements TokenDAO{

    @PersistenceContext(name = "mysql")
    EntityManager em;

    @Override
    public Token createNewToken(int idbank, int idaccount, String token) {

        Token t = new Token(idbank,idaccount,token);
        em.persist(t);
        return t;
    }

}
