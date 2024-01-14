package fr.pantheonsorbonne.ufr27.miage.cli;

import fr.pantheonsorbonne.ufr27.miage.camel.NotificationGateway;
import fr.pantheonsorbonne.ufr27.miage.dto.DemandeAuthentification;
import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.exception.TokenGenerationException;
import fr.pantheonsorbonne.ufr27.miage.service.CompteService;
import fr.pantheonsorbonne.ufr27.miage.service.CustomerService;
import fr.pantheonsorbonne.ufr27.miage.service.NotificationService;
import fr.pantheonsorbonne.ufr27.miage.service.TokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import fr.pantheonsorbonne.ufr27.miage.model.Customer;
import fr.pantheonsorbonne.ufr27.miage.model.Account;
import fr.pantheonsorbonne.ufr27.miage.model.Notification;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;

@ApplicationScoped
public class UserInterfaceCLIImpl implements UserInterfaceCLI {
    TextTerminal<?> terminal;
    TextIO textIO;

    @Inject
    NotificationService notificationService;
    @Inject
    CompteService compteService;
    @Inject
    CustomerService customerService;
    @Inject
    TokenService tokenService;
    @Inject
    NotificationGateway notificationGateway;

    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.bankName")
    String bankName;

    public User getUserInfoToBank(){
        terminal.println("Welcome to "+bankName+" bank !");

        String email = textIO.newStringInputReader().read("Your email ?");
        String password = textIO.newStringInputReader().withInputMasking(true).read("Your password ?");

        return new User(email,password);
    }
    @Override
    public void userFunctionalities(User user) throws IOException {
        List<String> functionalityNames = new ArrayList<>();
        for (Functionality functionality : Functionality.values()) {
            functionalityNames.add(functionality.name().toLowerCase());
        }
        String f = textIO.newStringInputReader().withPossibleValues(functionalityNames).read("Select an option");

        if(f.equals(Functionality.NOTIFICATION.name().toLowerCase())){
            this.respondNotification(user);
        }
    }

    private void respondNotification(User user) throws IOException {
        Customer customer = customerService.findCustomer(user.getEmail());
        Account account = compteService.findAccount(customer.getIdCustomer());

        Collection<Notification> notif = notificationService.notificationAuthorisationAvailableForAnAccount(account.getIdAccount());

            for(Notification n : notif){
                terminal.println(n.getTexte());
                String response = textIO.newStringInputReader().withPossibleValues(Arrays.asList("Yes", "No")).read("Select a response");
                /*
                if(response.equals("Yes")) {
                    try {
                        //DemandeAuthentification demandeAuthentification = tokenService.generateToken(user);
                        terminal.println("Token generated and sent: " ); //pour voir la génération de token
                    } catch (Exception e) {
                        terminal.println("Error generating token: " + e.getMessage());
                    }
                }
                 */
                notificationGateway.sendResponseSynchro(response,n,user);
                terminal.println("Message sent !");
            }
        }



    @Override
    public void accept(TextIO textIO, RunnerData runnerData) {
        this.textIO = textIO;
        terminal = textIO.getTextTerminal();
   }

    @Override
    public void showErrorMessage(String errorMessage) {
        terminal.getProperties().setPromptColor(Color.RED);
        terminal.println(errorMessage);
        terminal.getProperties().setPromptColor(Color.white);
    }

    @Override
    public void showSuccessMessage(String s) {
        terminal.getProperties().setPromptColor(Color.GREEN);
        terminal.println(s);
        terminal.getProperties().setPromptColor(Color.white);
    }
}
