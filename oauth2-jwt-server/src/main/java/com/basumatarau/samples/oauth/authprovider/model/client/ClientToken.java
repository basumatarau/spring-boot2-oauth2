package com.basumatarau.samples.oauth.authprovider.model.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClientToken {

    @Id
    private String id;
    private String tokenId;

    @Lob
    private byte[] token;
    private String authenticationId;
    private String username;
    private String clientId;
}
