package top.nextnet.dao;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import top.nextnet.model.Bank;

import java.util.Collections;
import java.util.List;

@ApplicationScoped

public class BankDAOimpl implements BankDAO{

    @PersistenceContext(name = "mysql")
    EntityManager em;

    @Override
    @Transactional
    public Bank findMatchingBank(String name) {
        try {
            Bank c = (Bank) em.createQuery("Select c from Bank c where c.name=:name").setParameter("name", name).getSingleResult();
            return c;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional
    public String getBankNameById(int bankId) {
        try {
            Bank bank = em.find(Bank.class, bankId);
            if (bank != null) {
                return bank.getName();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional
    public List<Bank> getAllBanksByUserId(int userId) {
        try {
            return em.createQuery(
                            "SELECT DISTINCT b FROM Bank b WHERE NOT EXISTS " +
                                    "(SELECT 1 FROM Account a WHERE a.idBank = b.idBank AND a.idUser = :userId)", Bank.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
