package com.api.passlogServer.credential;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "credentials")
@Table(name = "credentials")
public class Credentials implements Serializable {
    @Id
    @SequenceGenerator(
            name = "credential_sequence",
            sequenceName = "credential_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "credential_sequence"
    )
    @JsonProperty("id")
    private int id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("description")
    private String description;
    @JsonProperty("application")
    private String application;
    @JsonProperty("user_id")
    private int user_id;

    public Credentials() {
        this.username = "<Username>";
        this.password = "<Password>";
        this.description = "<Description>";
        this.application = "<Application>";
        this.user_id = 0;
    }

    public Credentials(String username, String password, String description, String application, int user_id){
        this.username = username;
        this.password = password;
        this.description = description;
        this.application = application;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                ", application='" + application + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
