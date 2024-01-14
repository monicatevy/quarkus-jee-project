package fr.pantheonsorbonne.ufr27.miage.cli;


import fr.pantheonsorbonne.ufr27.miage.dto.User;
import org.apache.camel.Body;
import org.apache.camel.Header;

import java.io.IOException;

public interface UserInterface {
    void showErrorMessage(String errorMessage);
    void showTest(User user) throws IOException;
    void processAuthorizationRequest(User user) throws IOException;
    void showSuccessMessage(String s);

}
