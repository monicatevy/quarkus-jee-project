package top.nextnet.camel.gateways;

import fr.pantheonsorbonne.ufr27.miage.dto.DemandeAuthorisation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.ConsumerTemplate;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.apache.camel.Exchange;
import top.nextnet.model.Bank;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class AuthorizationGatewayImpl implements top.nextnet.service.AuthorizationGateway {

    @Inject
    CamelContext context;

    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.jmsPrefix")
    String jmsPrefix;

    @Override
    public void sendAuthorizationRequest(Bank selectedBank, DemandeAuthorisation demandeAuthorisation) {
        try (ProducerTemplate producer = context.createProducerTemplate()) {
            Map<String, Object> headers = new HashMap<>();
            headers.put("bankName", selectedBank.getName());
            headers.put("bankGroup", selectedBank.getGroupName());
            headers.put("clientEmail", demandeAuthorisation.getUser().getEmail());
            producer.sendBodyAndHeaders("direct:cli", demandeAuthorisation, headers);
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


