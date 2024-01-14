package fr.pantheonsorbonne.ufr27.miage.exception;

public class TokenNoGenerationException extends Throwable {
    public TokenNoGenerationException() {
        super("Token non généré");
    }
}

