package top.nextnet.service;

import fr.pantheonsorbonne.ufr27.miage.dto.ReponseAuthorisation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import top.nextnet.dao.AccountDAO;
import top.nextnet.dao.BankDAO;
import top.nextnet.dao.TokenDAO;
import top.nextnet.dao.UserDAO;
import top.nextnet.exception.BankinAccountNotFoundException;
import top.nextnet.model.Account;
import top.nextnet.model.Bank;
import top.nextnet.model.User;

@ApplicationScoped
public class TokenDataServiceImpl implements TokenDataService {

    @Inject
    TokenDAO tokenDAO;
    @Inject
    AccountDAO accountDAO;
    @Inject
    BankDAO BankDAO;
    @Inject
    UserDAO userDAO;
    @Override
    @Transactional
    public void saveTokenData(ReponseAuthorisation responseAuth) throws BankinAccountNotFoundException {

        Bank bank = BankDAO.findMatchingBank(responseAuth.getUserBankName());
        User user = userDAO.findMatchingUser(responseAuth.getUser().getEmail());
        Account account = accountDAO.findMatchingAccount(user.getIdUser());

        tokenDAO.createNewToken(bank.getIdBank(), account.getIdUser(), responseAuth.getToken());
    }
}
