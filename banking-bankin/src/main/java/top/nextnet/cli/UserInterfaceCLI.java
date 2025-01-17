package top.nextnet.cli;

import fr.pantheonsorbonne.ufr27.miage.dto.ReponseAuthorisation;
import org.beryx.textio.TextIO;
import fr.pantheonsorbonne.ufr27.miage.dto.User;
import top.nextnet.model.Account;
import top.nextnet.model.Bank;

import java.util.List;
import java.util.function.BiConsumer;


public interface UserInterfaceCLI extends BiConsumer<TextIO, RunnerData>, UserInterface {

    User getUserInfoToBankin();
    User getUserInfoForBank(String bankName);
    Bank getUserBank(int userId);
    void displayUserOptions(top.nextnet.model.User user);
    void displayAccounts(List<Account> accounts);
}


