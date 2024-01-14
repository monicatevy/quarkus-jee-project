package fr.pantheonsorbonne.ufr27.miage.cli;

import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.service.CompteService;
import jakarta.inject.Inject;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.swing.SwingTextTerminal;
import picocli.CommandLine.Command;



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

        boolean isConnected = false;
        while (!isConnected) {
            try {
                User user = eCommerce.getUserInfoToBank();
                isConnected = compteService.login(user.getEmail(), user.getpwd());

                if (isConnected) {
                    terminal.println("Success ! Bienvenue !");
                    eCommerce.userFunctionalities(user);
                } else {
                    throw new Exception("Connexion échouée");
                }
            } catch (Exception e) {
                eCommerce.showErrorMessage(e.getMessage());
            }
        }


    }
}
