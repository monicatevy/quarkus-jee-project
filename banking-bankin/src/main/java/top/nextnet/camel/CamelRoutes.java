package top.nextnet.camel;


import fr.pantheonsorbonne.ufr27.miage.dto.ReponseAuthorisation;
import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.service.Constante;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import top.nextnet.cli.BankGroup;
import top.nextnet.cli.UserInterface;
import top.nextnet.service.TokenDataService;

@ApplicationScoped
public class CamelRoutes extends RouteBuilder {

    @ConfigProperty(name = "camel.routes.enabled", defaultValue = "true")
    boolean isRouteEnabled;

    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.jmsPrefix")
    String jmsPrefix;
    @Inject
    UserInterface eCommerce;
    @Inject
    TokenDataService tokenDataService;

    @Inject
    CamelContext camelContext;
    @Override
    public void configure() throws Exception {

        camelContext.setTracing(true);


        from("direct:cli")
                .log("Bank ID: ${header.bankGroup}, Message Body: ${body}")
                .choice()
                .when(header("bankGroup").isEqualTo(BankGroup.BPCE))
                .marshal().json()
                .to("sjms2:"+jmsPrefix+"requestSynch?exchangePattern=InOut")
                .otherwise()
                .marshal().jacksonXml()
                .to("sjms2:"+jmsPrefix+"requestSynch?exchangePattern=InOut")
        ;


        /*
                .when(header("bankGroup").isEqualTo(BankGroup.BPCE))
                .log("Request from BPCE bank. Using XML format.")
                .marshal().jaxb(User.class.getPackage().getName())
                .to("sjms2:" + jmsPrefix + "authorizationBPCE?exchangePattern=InOut")

                .otherwise()
                .log("Unknown bankGroup: ${header.bankGroup}")
                .end()

                 */

        from("sjms2:topic:receiveToken" + jmsPrefix + "?exchangePattern=InOut")
                .log("User email: ${header.UserEmail}, Message Body: ${body}")
                .unmarshal().json(ReponseAuthorisation.class)
                .bean(tokenDataService, "saveTokenData")
                .log("Token received from bank: ${body}")
                .to("direct:sentToken");




        //.to("direct:authorization");
        //.bean(eCommerce, "showTest").stop();


        /*onException(ExpiredTransitionalTicketException.class)
                .handled(true)
                .process(new ExpiredTransitionalTicketProcessor())
                .setHeader("success", simple("false"))
                .log("Clearning expired transitional ticket ${body}")
                .bean(ticketingService, "cleanUpTransitionalTicket");*/

       /*onException(UnsuficientQuotaForVenueException.class)
                .handled(true)
                .setHeader("success", simple("false"))
                .setBody(simple("Vendor has not enough quota for this venue"));


        onException(NoSuchTicketException.class)
                .handled(true)
                .setHeader("success", simple("false"))
                .setBody(simple("Ticket has expired"));

        onException(CustomerNotFoundException.NoSeatAvailableException.class)
                .handled(true)
                .setHeader("success", simple("false"))
                .setBody(simple("No seat is available"));*/


        /*from("sjms2:" + jmsPrefix + "booking?exchangePattern=InOut")//
                .autoStartup(isRouteEnabled)
                .log("ticker received: ${in.headers}")//
                .unmarshal().json(Booking.class)//
                .bean(bookingHandler, "book").marshal().json()
        ;


        from("sjms2:" + jmsPrefix + "ticket?exchangePattern=InOut")
                .autoStartup(isRouteEnabled)
                .unmarshal().json(ETicket.class)
                .bean(ticketingService, "emitTicket").marshal().json();


        from("direct:ticketCancel")
                .autoStartup(isRouteEnabled)
                .marshal().json()
                .to("sjms2:topic:" + jmsPrefix + "cancellation");*/

    }
}

 /*
import fr.pantheonsorbonne.ufr27.miage.dto.Booking;
import fr.pantheonsorbonne.ufr27.miage.dto.CancelationNotice;
import fr.pantheonsorbonne.ufr27.miage.dto.ETicket;
import fr.pantheonsorbonne.ufr27.miage.dto.TicketEmissionData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import top.nextnet.cli.UserInterface;
import top.nextnet.service.TicketingService;


import java.util.HashMap;

@ApplicationScoped
public class CamelRoutes extends RouteBuilder {

    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.jmsPrefix")
    String jmsPrefix;

    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.vendorId")
    Integer vendorId;

    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.smtp.user")
    String smtpUser;

    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.smtp.password")
    String smtpPassword;

    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.smtp.host")
    String smtpHost;

    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.smtp.port")
    String smtpPort;
    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.smtp.from")
    String smtpFrom;

    @Inject
    top.nextnet.camel.handler.BookingResponseHandler BookingResponseHandler;

    @Inject
    TicketingService ticketingService;

    @Inject
    UserInterface eCommerce;

    @Inject
    CamelContext camelContext;


    @Override
    public void configure() throws Exception {
        camelContext.setTracing(true);


        from("direct:cli")//
                .marshal().json()//, "onBookedResponseReceived"
                .to("sjms2:" + jmsPrefix + "booking?exchangePattern=InOut")//
                .choice()
                .when(header("success").isEqualTo(false))
                .setBody(simple("not enough quota for this vendor"))
                .bean(eCommerce, "showErrorMessage").stop()
                .otherwise()
                .unmarshal().json(Booking.class)
                .bean(BookingResponseHandler)
                .log("response received ${in.body}")
                .bean(ticketingService, "fillTicketsWithCustomerInformations")
                .split(body())
                .marshal().json(ETicket.class)
                .to("sjms2:" + jmsPrefix + "ticket?exchangePattern=InOut")
                .choice()
                .when(header("success").isEqualTo(false))
                .bean(eCommerce, "showErrorMessage").stop()
                .otherwise()
                .unmarshal().json(TicketEmissionData.class)
                .bean(ticketingService, "notifyCreatedTicket");


        from("sjms2:topic:" + jmsPrefix + "cancellation")
                .log("cancellation notice ${body} ${headers}")
                .filter(header("vendorId").isEqualTo(vendorId))

                .unmarshal().json(CancelationNotice.class)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {

                        CancelationNotice notice = exchange.getMessage().getMandatoryBody(CancelationNotice.class);
                        exchange.getMessage().setHeaders(new HashMap<>());
                        exchange.getMessage().setHeader("to", notice.getEmail());
                        exchange.getMessage().setHeader("from", smtpFrom);
                        exchange.getMessage().setHeader("contentType", "text/html");
                        exchange.getMessage().setHeader("subject", "cancellation notice for venue");
                        exchange.getMessage().setBody("Dear Customer,\n\n Venue for your ticket " + notice.getTicketId() + " has been cancelled.\n\n Contact vendor for refund");
                    }
                })
                .log("cancellation notice ${body} ${headers}")
                .to("smtps:" + smtpHost + ":" + smtpPort + "?username=" + smtpUser + "&password=" + smtpPassword + "&contentType=")
                .bean(eCommerce, "showErrorMessage");


    }
}
*/