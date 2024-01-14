package top.nextnet.cli;


import fr.pantheonsorbonne.ufr27.miage.dto.DemandeAuthorisation;
import fr.pantheonsorbonne.ufr27.miage.dto.User;
import jakarta.inject.Inject;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.swing.SwingTextTerminal;
import picocli.CommandLine;

import top.nextnet.model.Account;
import top.nextnet.model.Bank;
import top.nextnet.service.*;

@CommandLine.Command(name = "greeting", mixinStandardHelpOptions = true)
public class Main implements Runnable {


    @Inject
    UserInterfaceCLI eCommerce;

    @Inject
    AuthorizationGateway authorizationGateway;

    @Inject
    UserService userService;
    @Inject
    AccountService accountService;

    @Inject
    ConnexionService connexionService;

    @Override
    public void run() {

        System.setProperty(TextIoFactory.TEXT_TERMINAL_CLASS_PROPERTY, SwingTextTerminal.class.getName());
        TextIO textIO = TextIoFactory.getTextIO();
        var terminal = TextIoFactory.getTextTerminal();

        eCommerce.accept(textIO, new RunnerData(""));

        boolean isConnected = false;
        while (!isConnected) {
            try {
                User user = eCommerce.getUserInfoToBankin();
                isConnected = connexionService.login(user.getEmail(), user.getPwd());

                if (isConnected) {
                    userService.getUserByEmail(user.getEmail());
                    top.nextnet.model.User connectedUser = userService.getUserByEmail(user.getEmail());
                    eCommerce.displayUserOptions(connectedUser);

                    int userInput = textIO.newIntInputReader().read("Enter your choice:");
                    switch (userInput) {
                        case 1 -> {
                            Bank selectedBank = eCommerce.getUserBank();
                            User userBank = eCommerce.getUserInfoForBank(selectedBank.getName());
                            DemandeAuthorisation authorizationRequest = new DemandeAuthorisation(userBank, "Add account authorization request");
                            authorizationGateway.sendAuthorizationRequest(selectedBank.getGroupName(), authorizationRequest);
                            boolean authorized = authorizationGateway.receiveAuthorizationResponse();
                            if(authorized){
                                eCommerce.showSuccessMessage("Authorization granted!");
                                Account newAccount = new Account();
                                newAccount.setIdBank(selectedBank.getIdBank());
                                newAccount.setIdUser(connectedUser.getIdUser());
                                accountService.addAccount(newAccount);
                                eCommerce.showSuccessMessage("Your "+ selectedBank.getName() +" account has been created !");
                            }
                            else{
                                eCommerce.showErrorMessage("Authorization denied.");
                            }
                        }
                        case 2 -> {
                            eCommerce.displayAccounts(accountService.findAllAccountsByUserId(connectedUser.getIdUser()));
                            eCommerce.displayUserOptions(connectedUser);
                        }
                        default -> terminal.println("Invalid choice.");
                    }

                } else {
                    throw new Exception("Connexion échouée");
                }
            } catch (Exception e) {
                eCommerce.showErrorMessage(e.getMessage());
            }
        }



    }

}







 /*
import fr.pantheonsorbonne.ufr27.miage.dto.Booking;
import jakarta.inject.Inject;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.swing.SwingTextTerminal;
import picocli.CommandLine.Command;
import top.nextnet.service.BookingGateway;

@Command(name = "greeting", mixinStandardHelpOptions = true)
public class Main implements Runnable {


    @Inject
    UserInterfaceCLI eCommerce;

    @Inject
    BookingGateway bookingGateway;

    @Override
    public void run() {

        System.setProperty(TextIoFactory.TEXT_TERMINAL_CLASS_PROPERTY, SwingTextTerminal.class.getName());
        TextIO textIO = TextIoFactory.getTextIO();
        var terminal = TextIoFactory.getTextTerminal();

        eCommerce.accept(textIO, new RunnerData(""));


        while (true) {
            try {
                eCommerce.displayAvailableGigsToCli();
                Booking booking = eCommerce.getBookingFromOperator();
                bookingGateway.sendBookingOrder(booking.getStandingTicketsNumber(), booking.getSeatingTicketsNumber(), booking.getVenueId());
            } catch (Exception e) {
                eCommerce.showErrorMessage(e.getMessage());
            }
        }


    }

}
*/