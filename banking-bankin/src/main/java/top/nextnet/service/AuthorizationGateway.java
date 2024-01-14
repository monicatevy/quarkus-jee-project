package top.nextnet.service;

import fr.pantheonsorbonne.ufr27.miage.dto.DemandeAuthorisation;
import fr.pantheonsorbonne.ufr27.miage.dto.User;

public interface AuthorizationGateway {
    void sendAuthorizationRequest(String bankGroup, DemandeAuthorisation demandeAuthorisation);
    boolean receiveAuthorizationResponse();
}
