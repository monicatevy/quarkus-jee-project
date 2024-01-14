package fr.pantheonsorbonne.ufr27.miage.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DemandeAuthentification {
    String texte;
    User user;
    String token;
    public DemandeAuthentification(User user, String token, String texte){
        this.user = user;
        this.texte = texte;
        this.token = token;
    }

    public DemandeAuthentification(User user, String token){
        this.user = user;
        this.texte = texte;
        this.token = token;
    }
    public DemandeAuthentification(){}
    public void setUser(User user){
        this.user = user;
    }
    public User getUser(){
        return this.user;
    }
    public void setTexte(String texte){
        this.texte = texte;
    }
    public String getTexte(){
        return this.texte;
    }

    public void setToken(String token){
        this.token = token;
    }
    public String getToken(){
        return this.token;
    }
}
