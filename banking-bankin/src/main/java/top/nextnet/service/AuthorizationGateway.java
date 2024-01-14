package top.nextnet.service;

import fr.pantheonsorbonne.ufr27.miage.dto.DemandeAuthorisation;
import fr.pantheonsorbonne.ufr27.miage.dto.User;
import top.nextnet.model.Bank;

public interface AuthorizationGateway {
    void sendAuthorizationRequest(Bank selectedBank, DemandeAuthorisation demandeAuthorisation);
    boolean receiveAuthorizationResponse();
}
