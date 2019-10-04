package com.basumatarau.samples.oauth.authprovider.model.client;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AccessToken {

    @Id
    @EqualsAndHashCode.Exclude
    private String tokenId;

    @Lob
    private byte[] token;
    private String authenticationId;
    private String username;
    private String clientId;

    @Lob
    private byte[] authentication;
    private String refreshToken;
}
