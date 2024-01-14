package fr.pantheonsorbonne.ufr27.miage.cli;


import fr.pantheonsorbonne.ufr27.miage.dto.DemandeAuthorisation;
import fr.pantheonsorbonne.ufr27.miage.dto.User;
import org.apache.camel.Body;
import org.apache.camel.Header;

public interface UserInterface {
    void showErrorMessage(String errorMessage);
    boolean getAuthorizationRequestResponse(DemandeAuthorisation demandeAuthorisation);
    void showSuccessMessage(String s);

}
