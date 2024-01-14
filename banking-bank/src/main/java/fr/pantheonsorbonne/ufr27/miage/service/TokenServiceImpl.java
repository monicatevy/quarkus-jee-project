package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dto.DemandeAuthentification;
import fr.pantheonsorbonne.ufr27.miage.dto.DemandeAuthorisation;
import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.exception.TokenGenerationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Date;

@ApplicationScoped
public class TokenServiceImpl implements TokenService {
    private final String secretKey = "YourVeryLongSecretKeyForJWTGeneration"; // Replace with a secure key

    @Override
    @Transactional
    public DemandeAuthentification generateToken(User user) throws TokenGenerationException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String token = JWT.create()
                    .withSubject(user.getEmail())
                    .withIssuedAt(new Date())
                    .sign(algorithm);

            // Assuming the text for DemandeAuthorisation is the JWT token
            return new DemandeAuthentification(user, token);
        } catch (Exception e) {
            throw new TokenGenerationException();
        }
    }



}
