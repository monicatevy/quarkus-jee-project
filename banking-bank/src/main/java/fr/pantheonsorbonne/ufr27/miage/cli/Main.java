package fr.pantheonsorbonne.ufr27.miage.cli;

import fr.pantheonsorbonne.ufr27.miage.camel.NotificationGateway;
import fr.pantheonsorbonne.ufr27.miage.dto.DemandeAuthorisation;
import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.service.CompteService;
import fr.pantheonsorbonne.ufr27.miage.service.NotificationService;
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
    @Inject
    NotificationGateway notificationGateway;

    @Override
    public void run() {

        System.setProperty(TextIoFactory.TEXT_TERMINAL_CLASS_PROPERTY, SwingTextTerminal.class.getName());
        TextIO textIO = TextIoFactory.getTextIO();
        var terminal = TextIoFactory.getTextTerminal();

        eCommerce.accept(textIO, new RunnerData(""));

        //  while(true){
        try {
            /*
            User u = new User("aze@gmail.com","azerty");
            DemandeAuthorisation d = new DemandeAuthorisation(u,"Demande de synchro");
            notificationGateway.sendTest(d);
              */
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
        //  }
    }

}