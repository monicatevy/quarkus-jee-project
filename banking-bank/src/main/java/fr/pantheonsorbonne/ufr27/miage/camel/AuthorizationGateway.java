package fr.pantheonsorbonne.ufr27.miage.camel;

import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.exception.TokenGenerationException;
import fr.pantheonsorbonne.ufr27.miage.service.TokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

import java.io.IOException;

@ApplicationScoped
public class AuthorizationGateway {
    @Inject
    CamelContext context;

    @Inject
    TokenService tokenService;

    public void sendResponseRequest(String response, User user, String bankName) throws IOException {
        try (ProducerTemplate producerTemplate = context.createProducerTemplate()) {
            if (response.equals("Yes")) {
                String token = tokenService.generateToken(user.getEmail());
                producerTemplate.sendBodyAndHeader("direct:sendToken", token, "bankName", bankName);
            }
        }
        catch (TokenGenerationException e) {
            throw new RuntimeException(e);
        }
    }

}

