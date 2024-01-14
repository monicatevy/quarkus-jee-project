package top.nextnet.service;

import top.nextnet.exception.BankinAccountNotFoundException;
import top.nextnet.model.Account;
import top.nextnet.model.Bank;

import java.util.List;

public interface AccountService {
    void addAccount(Account account);
    List<Account> findAllAccountsByUserId(int userId) throws BankinAccountNotFoundException;
}