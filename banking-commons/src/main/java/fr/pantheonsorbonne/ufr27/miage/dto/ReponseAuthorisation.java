package fr.pantheonsorbonne.ufr27.miage.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReponseAuthorisation {
    String token;
    User user;
    String bankName;
    public ReponseAuthorisation(User user, String bankName, String token){
        this.user = user;
        this.token = token;
        this.bankName = bankName;
    }
    public void setUser(User user){
        this.user = user;
    }
    public User getUser(){
        return this.user;
    }
    public void setTexte(String texte){
        this.token = token;
    }
    public String getTexte(){
        return this.token;
    }

    public void setUserBankName(String bankName){
        this.bankName = bankName;
    }
    public void getUserBankName(String bankName){
        this.bankName = bankName;
    }
}
