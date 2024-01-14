package fr.pantheonsorbonne.ufr27.miage.camel;

import fr.pantheonsorbonne.ufr27.miage.dto.DemandeAuthentification;
import fr.pantheonsorbonne.ufr27.miage.dto.DemandeAuthorisation;
import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.exception.TokenGenerationException;
import fr.pantheonsorbonne.ufr27.miage.model.Notification;
import fr.pantheonsorbonne.ufr27.miage.service.NotificationService;
import fr.pantheonsorbonne.ufr27.miage.service.TokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;

@ApplicationScoped
public class NotificationGateway {
    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.jmsPrefix")
    String jmsPrefix;
    @Inject
    NotificationService notificationService;

    @Inject
    TokenService tokenService;
    @Inject
    CamelContext camelContext;

    public void sendResponseSynchro(String response, Notification n, User user) throws IOException {
        try (ProducerTemplate producerTemplate = camelContext.createProducerTemplate()) {
            if(response == "Yes"){
                producerTemplate.sendBodyAndHeader("direct:responseSynchro", "Authorization accepted ! token generated", "success","true");
                DemandeAuthentification demandeAuthentification = tokenService.generateToken(user);
             }else{
                producerTemplate.sendBodyAndHeader("direct:responseSynchro","Authorization refused","success","false");
            }
            notificationService.updateNotificationHandle(n.getIdNotification());
        } catch (Exception | TokenGenerationException e){
            throw new RuntimeException(e);
        }
    }


    public void sendTest(DemandeAuthorisation demandeAuthorisation){
        try (ProducerTemplate producerTemplate = camelContext.createProducerTemplate()) {
            producerTemplate.sendBody("direct:test",demandeAuthorisation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}