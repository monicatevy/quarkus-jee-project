package top.nextnet.cli;


import fr.pantheonsorbonne.ufr27.miage.cli.Functionality;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
//import top.nextnet.resource.VendorService;

import fr.pantheonsorbonne.ufr27.miage.dto.User;
import top.nextnet.model.Account;
import top.nextnet.model.Bank;
import top.nextnet.service.BankService;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserInterfaceCLIImpl implements UserInterfaceCLI {
    @Inject
    BankService bankService;
    TextTerminal<?> terminal;
    TextIO textIO;


    public User getUserInfoToBankin(){
        terminal.println();
        terminal.println("Welcome to bankin !");

        String email = textIO.newStringInputReader().read("Your email ?");
        String password = textIO.newStringInputReader().withInputMasking(true).read("Your password ?");

        return new User(email,password);
    }

    public User getUserInfoForBank(String bankName){
        terminal.println();
        terminal.println("Bank account information for " + bankName);
        String email = textIO.newStringInputReader().read("Email: ");
        String password = textIO.newStringInputReader().withInputMasking(true).read("Password: ");
        return new User(email,password);
    }

    public Bank getUserBank(){
        List<Bank> banks = bankService.getAllBanks();
        if (banks.isEmpty()) {
            terminal.println("No banks available.");
        } else {
            terminal.println();
            terminal.println("Our partner banks: ");
            for (int i = 0; i < banks.size(); i++) {
                terminal.println("(" + (i + 1) + ") " + banks.get(i).getName());
            }
        }
        int bankInput = textIO.newIntInputReader().withMinVal(1).withMaxVal(banks.size()).read("Enter your bank: ");
        return banks.get(bankInput - 1);
    }

    public void displayAccounts(List<Account> accounts) {
        if (accounts.isEmpty()) {
            terminal.println("No account found");
        } else {
            terminal.println("");
            terminal.println("My accounts:");
            String bankName = "";
            for (Account account : accounts) {
                bankName = bankService.getBankNameById(account.getIdBank());
                terminal.println("- Account ID: " + account.getIdAccount() + bankName);
            }
        }
    }

    @Override
    public void displayUserOptions(top.nextnet.model.User user){
        terminal.println();
        terminal.println("Welcome "+user.getFirstName()+" "+user.getLastName());
        terminal.println();
        terminal.println("Menu:");
        terminal.println("(1) add account");
        terminal.println("(2) view my accounts");
    }

    @Override
    public void accept(TextIO textIO, RunnerData runnerData) {
        this.textIO = textIO;
        terminal = textIO.getTextTerminal();
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        terminal.getProperties().setPromptColor(Color.RED);
        terminal.println("");
        terminal.println(errorMessage);
        terminal.getProperties().setPromptColor(Color.white);
    }

    @Override
    public void showSuccessMessage(String s) {
        terminal.getProperties().setPromptColor(Color.GREEN);
        terminal.println("");
        terminal.println(s);
        terminal.getProperties().setPromptColor(Color.white);
    }
}

