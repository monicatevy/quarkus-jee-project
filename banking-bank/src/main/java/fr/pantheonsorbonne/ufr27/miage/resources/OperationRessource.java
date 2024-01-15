package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.model.Operation;
import fr.pantheonsorbonne.ufr27.miage.service.OperationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Path("/vendor")
public class OperationRessource {

    @Inject
    OperationService operationService;

    @Path("/{idAccount}")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Operation> getOperations(@PathParam("idAccount") int idAccount) {
        return operationService.getOperationsByAccountId(idAccount);
    }

    @Path("/{idAccount}/transactions")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Operation> getTransactionsByOperation(@PathParam("idAccount") int idAccount) {
        return operationService.getOperationsByAccountId(idAccount);
    }

}
