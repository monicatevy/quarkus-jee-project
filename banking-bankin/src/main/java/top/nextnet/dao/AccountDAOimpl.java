package top.nextnet.dao;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import top.nextnet.exception.BankinAccountNotFoundException;
import top.nextnet.model.Account;

import java.util.List;

@ApplicationScoped
public class AccountDAOimpl implements AccountDAO {

    @PersistenceContext(name="mysql")
    EntityManager em;

    public Account findMatchingAccount(int idUser) throws BankinAccountNotFoundException {
        try {
            List<Account> accounts = em.createQuery("Select a from Account a " +
                            " where a.idUser=:idUser", Account.class)
                    .setParameter("idUser", idUser)
                    .getResultList();

            if (!accounts.isEmpty()) {
                return accounts.get(0);
            } else {
                throw new BankinAccountNotFoundException();
            }
        } catch (NoResultException e) {
            throw new BankinAccountNotFoundException();
        }
    }

    @Override
    @Transactional
    public List<Account> findAllAccountsByUserId(int idUser) throws BankinAccountNotFoundException {
        try {
            List<Account> accounts = em.createQuery("SELECT a FROM Account a WHERE a.idUser = :idUser", Account.class)
                    .setParameter("idUser", idUser)
                    .getResultList();
            if (accounts.isEmpty()) {
                throw new BankinAccountNotFoundException();
            }
            return accounts;
        } catch (NoResultException e) {
            throw new BankinAccountNotFoundException();
        }
    }


    @Transactional
    public void addAccount(Account account) {
        try {
            em.persist(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}