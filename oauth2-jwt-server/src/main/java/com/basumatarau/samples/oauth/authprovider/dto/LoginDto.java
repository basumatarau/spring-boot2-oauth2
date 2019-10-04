package com.basumatarau.samples.oauth.authprovider.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LoginDto {

    private final String email;

    private final String rawPassword;

    @JsonCreator
    public LoginDto(@JsonProperty("email") String email,
                    @JsonProperty("rawPassword") String rawPassword) {
        this.email = email;
        this.rawPassword = rawPassword;
    }
}
