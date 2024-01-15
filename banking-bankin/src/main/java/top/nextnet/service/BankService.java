package top.nextnet.service;

import top.nextnet.model.Bank;

import java.util.List;

public interface BankService {
    List<Bank> getAllBankByUserId(int userId);
    String getBankNameById(int bankId);

}
