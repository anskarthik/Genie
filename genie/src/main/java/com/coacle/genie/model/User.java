package com.coacle.genie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    private String email;

    private String name;

    @JsonIgnore
    private String password;

    private boolean isEnabled = false;

    @ElementCollection
    private List<String> roles;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<VerificationToken> verificationTokens;

}