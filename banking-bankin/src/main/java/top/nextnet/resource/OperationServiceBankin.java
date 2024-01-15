package top.nextnet.resource;

import fr.pantheonsorbonne.ufr27.miage.model.Operation;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Collection;

@Path("/operation")
@RegisterRestClient(configKey = "vendor-api")
public interface OperationServiceBankin {
    @Path("/{idAccount}")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Collection<Operation> getOperations(@PathParam("idAccount") int idAccount);
}