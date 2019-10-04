package com.basumatarau.samples.oauth.authprovider.repository;

import com.basumatarau.samples.oauth.authprovider.model.client.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, String> {
    Optional<AccessToken> findByTokenId(String tokenId);
    Optional<AccessToken> findByRefreshToken(String acc);
    Optional<AccessToken> findByAuthenticationId(String tokenId);

    List<AccessToken> findByClientId(String clientId);
    List<AccessToken> findByClientIdAndUsername(String clientId, String username);

    @Query(value = "delete from AccessToken at where at.refreshToken=:refreshTokenId")
    void deleteByRefreshTokenId(@Param("refreshTokenId") String refreshTokenId);
}
