package top.nextnet.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import top.nextnet.dao.AccountDAO;
import top.nextnet.dao.BankDAO;
import top.nextnet.exception.BankinAccountNotFoundException;
import top.nextnet.model.Account;
import top.nextnet.model.Bank;

import java.util.Collections;
import java.util.List;


@ApplicationScoped

public class AccountServiceImpl implements AccountService {

    @Inject
    AccountDAO accountDAO;

    @Override
    @Transactional
    public void addAccount(Account account) {
        try {
            accountDAO.addAccount(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public List<Account> findAllAccountsByUserId(int userId) throws BankinAccountNotFoundException {
        return accountDAO.findAllAccountsByUserId(userId);
    }
}