package fr.pantheonsorbonne.ufr27.miage.cli;

import fr.pantheonsorbonne.ufr27.miage.dto.User;
import org.apache.camel.Body;
import org.apache.camel.Header;
import org.beryx.textio.TextIO;

import java.io.IOException;
import java.util.function.BiConsumer;

public interface UserInterfaceCLI extends BiConsumer<TextIO, RunnerData>, UserInterface {

    User getUserInfoToBank();
    void userFunctionalities(User user);
    void processAuthorizationRequest(User user) throws IOException;

    void showTest(User user) throws IOException;

    void showSuccessMessage(String s);

}
