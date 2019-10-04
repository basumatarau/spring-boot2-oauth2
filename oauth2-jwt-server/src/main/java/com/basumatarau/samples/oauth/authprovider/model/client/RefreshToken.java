package com.basumatarau.samples.oauth.authprovider.model.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RefreshToken {

    @Id
    private String tokenId;
    private byte[] token;
    private byte[] authentication;
}
