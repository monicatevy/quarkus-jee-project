package top.nextnet.service;
import fr.pantheonsorbonne.ufr27.miage.dto.ReponseAuthorisation;
import top.nextnet.exception.BankinAccountNotFoundException;

public interface TokenDataService {
    void saveTokenData(ReponseAuthorisation responseAuth) throws BankinAccountNotFoundException;
}
