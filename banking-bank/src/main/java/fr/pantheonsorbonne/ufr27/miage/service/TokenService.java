package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dto.DemandeAuthentification;
import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.exception.TokenGenerationException;

public interface TokenService {
    DemandeAuthentification generateToken(User user) throws TokenGenerationException;
}
