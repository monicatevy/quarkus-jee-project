package top.nextnet.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import top.nextnet.dao.BankDAO;
import top.nextnet.exception.BankinAccountNotFoundException;
import top.nextnet.model.Bank;

import java.util.Collections;
import java.util.List;


@ApplicationScoped

public class BankServiceImpl implements BankService {

    @Inject
    BankDAO bankDAO;

    @Override
    @Transactional
    public List<Bank> getAllBankByUserId(int userId){
        try {
            return bankDAO.getAllBanksByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public String getBankNameById(int bankId) {
        try {
            return bankDAO.getBankNameById(bankId);
        } catch (Exception e) {
            return "";
        }
    }
}
