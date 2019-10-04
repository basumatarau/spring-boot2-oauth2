package com.basumatarau.samples.oauth.authprovider.repository;

import com.basumatarau.samples.oauth.authprovider.model.client.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByTokenId(String tokenId);
}
