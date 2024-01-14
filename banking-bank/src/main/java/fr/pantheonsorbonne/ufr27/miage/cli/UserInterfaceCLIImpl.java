package fr.pantheonsorbonne.ufr27.miage.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.camel.AuthorizationGateway;
import fr.pantheonsorbonne.ufr27.miage.camel.NotificationGateway;
import fr.pantheonsorbonne.ufr27.miage.dto.DemandeAuthorisation;
import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.exception.TokenGenerationException;
import fr.pantheonsorbonne.ufr27.miage.service.CompteService;
import fr.pantheonsorbonne.ufr27.miage.service.CustomerService;
import fr.pantheonsorbonne.ufr27.miage.service.NotificationService;
import fr.pantheonsorbonne.ufr27.miage.service.TokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Body;
import org.apache.camel.Header;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import fr.pantheonsorbonne.ufr27.miage.model.Customer;
import fr.pantheonsorbonne.ufr27.miage.model.Account;
import fr.pantheonsorbonne.ufr27.miage.model.Notification;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

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
    AuthorizationGateway authorizationGateway;

    @Inject
    NotificationGateway notificationGateway;
    @Inject
    TokenService tokenService;
    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.bankName")
    String bankName;

    public User getUserInfoToBank(){
        terminal.println("Welcome to "+bankName+" bank !");

        String email = textIO.newStringInputReader().read("Your email ?");
        String password = textIO.newStringInputReader().withInputMasking(true).read("Your password ?");

        return new User(email,password);
    }
    @Override
    public void userFunctionalities(User user){
        List<String> functionalityNames = new ArrayList<>();
        for (Functionality functionality : Functionality.values()) {
            functionalityNames.add(functionality.name().toLowerCase());
        }
        String f = textIO.newStringInputReader().withPossibleValues(functionalityNames).read("Select an option");

        if(f.equals(Functionality.NOTIFICATION.name().toLowerCase())){
            this.respondNotification(user);
        }
    }

    private void respondNotification(User user){
        Customer customer = customerService.findCustomer(user.getEmail());
        Account account = compteService.findAccount(customer.getIdCustomer());

        Collection<Notification> notif = notificationService.notificationAuthorisationAvailableForAnAccount(account.getIdAccount());

        if(notif != null){
            for(Notification n : notif) {
                terminal.println(n.getTexte());
                String response = textIO.newStringInputReader().withPossibleValues(Arrays.asList("Yes", "No")).read("Select a response");
                // notificationGateway.sendResponseSynchro(response,n);
                terminal.println("\n==================\n");
                terminal.println("Message sent !\n");
            }

            }else{
            terminal.println("\n==================\n");
            terminal.println("Empty notification");
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

    public boolean getAuthorizationRequestResponse(DemandeAuthorisation demandeAuthorisation) {
        //showAuthorizationRequest(demandeAuthorisation.getUser().getEmail());
        //return getClientResponse();
        return false;
    }

    public void processAuthorizationRequest(User user) throws IOException {
        terminal.println();
        terminal.println();
        terminal.println("--- AUTHORIZATION FROM BANKIN ---");
        terminal.println("request for: " + user);
        terminal.println();
        terminal.println("--- Would you like to give permission?  ---");
        terminal.println();
        String response = textIO.newStringInputReader().withPossibleValues(Arrays.asList("Yes", "No")).read("Select a response");
        authorizationGateway.sendResponseRequest(response,user,bankName);
        terminal.println("Authorisation sent !");
    }


    @Override
    public void showSuccessMessage(String s) {
        terminal.getProperties().setPromptColor(Color.GREEN);
        terminal.println(s);
        terminal.getProperties().setPromptColor(Color.white);
    }
}
