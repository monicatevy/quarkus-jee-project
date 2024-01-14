package fr.pantheonsorbonne.ufr27.miage.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class User implements Serializable {
    String email;
    String pwd;

    public User() {}

    public User(String email, String pwd){
        this.email = email;
        this.pwd = pwd;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getpwd(){
        return this.pwd;
    }
    public void setPwd(){
        this.pwd = pwd;
    }
}
