package fr.pantheonsorbonne.ufr27.miage.cli;

import fr.pantheonsorbonne.ufr27.miage.dao.TransactionDAO;
import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.service.CompteService;
import jakarta.inject.Inject;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.swing.SwingTextTerminal;
import picocli.CommandLine.Command;

import java.util.Calendar;


@Command(name = "greeting", mixinStandardHelpOptions = true)
public class Main implements Runnable {


    @Inject
    UserInterfaceCLI eCommerce;

    @Inject
    CompteService compteService;

    @Override
    public void run() {

        System.setProperty(TextIoFactory.TEXT_TERMINAL_CLASS_PROPERTY, SwingTextTerminal.class.getName());
        TextIO textIO = TextIoFactory.getTextIO();
        var terminal = TextIoFactory.getTextTerminal();

        eCommerce.accept(textIO, new RunnerData(""));
            while(true){
                try {
                    User user = eCommerce.getUserInfoToBank();
                    if(compteService.login(user.getEmail(), user.getPwd()) != null){
                        terminal.println("Success ! Bienvenue !");
                        while(true){
                            eCommerce.userFunctionalities(user);
                        }
                    }else{
                        throw new Exception("Connexion échoué");
                    }
                } catch (Exception e) {
                    eCommerce.showErrorMessage(e.getMessage());
                }
          }
    }

}
