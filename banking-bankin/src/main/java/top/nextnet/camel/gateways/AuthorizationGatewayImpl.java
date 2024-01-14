package top.nextnet.camel.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.dto.DemandeAuthorisation;
import fr.pantheonsorbonne.ufr27.miage.dto.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.ConsumerTemplate;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.apache.camel.Exchange;
import java.io.IOException;

@ApplicationScoped
public class AuthorizationGatewayImpl implements top.nextnet.service.AuthorizationGateway {

    @Inject
    CamelContext context;

    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.jmsPrefix")
    String jmsPrefix;

    @Override
    public void sendAuthorizationRequest(String bankGroup, DemandeAuthorisation demandeAuthorisation) {
        try (ProducerTemplate producer = context.createProducerTemplate()) {
            producer.sendBodyAndHeader("direct:cli", demandeAuthorisation, "bankGroup", bankGroup);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean receiveAuthorizationResponse() {
        try (ConsumerTemplate consumer = context.createConsumerTemplate()) {
            Exchange exchange = consumer.receive("sjms2:topic:authorizationResponse" + jmsPrefix);
            return exchange.getIn().getHeader("authorized", Boolean.class);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}


