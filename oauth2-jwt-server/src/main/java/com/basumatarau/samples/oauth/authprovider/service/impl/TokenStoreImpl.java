package com.basumatarau.samples.oauth.authprovider.service.impl;

import com.basumatarau.samples.oauth.authprovider.model.client.AccessToken;
import com.basumatarau.samples.oauth.authprovider.model.client.RefreshToken;
import com.basumatarau.samples.oauth.authprovider.repository.AccessTokenRepository;
import com.basumatarau.samples.oauth.authprovider.repository.RefreshTokenRepository;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.common.util.SerializationUtils.deserialize;
import static org.springframework.security.oauth2.common.util.SerializationUtils.serialize;

@Service
public class TokenStoreImpl implements TokenStore {

    private final AccessTokenRepository accessTokenRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final AuthenticationKeyGenerator authenticationKeyGenerator;

    public TokenStoreImpl(AccessTokenRepository accessTokenRepository,
                          RefreshTokenRepository refreshTokenRepository,
                          AuthenticationKeyGenerator authenticationKeyGenerator) {
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.authenticationKeyGenerator = authenticationKeyGenerator;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken oAuth2AccessToken) {
        return readAuthentication(oAuth2AccessToken.getValue());
    }

    //access token id generated at authorization server (to look up the access token by the token
    // id the id has to be extracted from the access token)
    @Override
    public OAuth2Authentication readAuthentication(String token) {
        final String tokenId = extractTokenKey(token);

        return accessTokenRepository
                .findByTokenId(tokenId)
                .map(accessToken ->deserializeAuthentication(accessToken.getAuthentication()))
                .orElse(null);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken oAuth2AccessToken,
                                 OAuth2Authentication oAuth2Authentication) {
        String refreshToken = null;
        if (oAuth2AccessToken.getRefreshToken() != null) {
            refreshToken = oAuth2AccessToken.getRefreshToken().getValue();
        }
        if (readAccessToken(oAuth2AccessToken.getValue()) != null) {
            removeAccessToken(oAuth2AccessToken);
        }

        accessTokenRepository.save(
                AccessToken.builder()
                        .tokenId(extractTokenKey(oAuth2AccessToken.getValue()))
                        .token(serializeAccessToken(oAuth2AccessToken))
                        .authentication(serializeAuthentication(oAuth2Authentication))
                        .authenticationId(authenticationKeyGenerator.extractKey(oAuth2Authentication))
                        .clientId(oAuth2Authentication.getOAuth2Request().getClientId())
                        .username(oAuth2Authentication.isClientOnly() ? null : oAuth2Authentication.getName())
                        .refreshToken(extractTokenKey(refreshToken))
                        .build()
        );
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {

        final String tokenId = extractTokenKey(tokenValue);

        return accessTokenRepository
                .findByTokenId(tokenId)
                .map(accessToken -> deserializeAccessToken(accessToken.getToken()))
                .orElse(null);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {
        accessTokenRepository
                .findByTokenId(extractTokenKey(oAuth2AccessToken.getValue()))
                .ifPresent(accessTokenRepository::delete);
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken,
                                  OAuth2Authentication oAuth2Authentication) {
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .tokenId(extractTokenKey(oAuth2RefreshToken.getValue()))
                        .token(serializeRefreshToken(oAuth2RefreshToken))
                        .authentication(serializeAuthentication(oAuth2Authentication))
                        .build()
        );
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String refreshTokenValue) {
        return refreshTokenRepository
                .findByTokenId(extractTokenKey(refreshTokenValue))
                .map(refreshToken -> deserializeRefreshToken(refreshToken.getToken()))
                .orElse(null);
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        return refreshTokenRepository
                .findByTokenId(extractTokenKey(oAuth2RefreshToken.getValue()))
                .map(
                        refreshToken -> deserializeAuthentication(refreshToken.getAuthentication())
                )
                .orElse(null);
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        refreshTokenRepository
                .findByTokenId(
                        extractTokenKey(oAuth2RefreshToken.getValue())
                )
                .ifPresent(refreshTokenRepository::delete);
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        accessTokenRepository
                .findByRefreshToken(
                        extractTokenKey(oAuth2RefreshToken.getValue())
                )
                .ifPresent(accessTokenRepository::delete);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication oAuth2Authentication) {
        OAuth2AccessToken[] accessTokens = new OAuth2AccessToken[]{null};
        final String authenticationId = authenticationKeyGenerator.extractKey(oAuth2Authentication);

        accessTokenRepository
                .findByAuthenticationId(authenticationId)
                .map(AccessToken::getToken)
                .map(this::deserializeAccessToken)
                .filter(oAuth2AccessToken ->
                        !authenticationId.equals(
                                authenticationKeyGenerator.extractKey(readAuthentication(oAuth2AccessToken))
                        )
                )
                .ifPresent(
                        oAuth2AccessToken -> {
                            removeAccessToken(oAuth2AccessToken);
                            storeAccessToken(oAuth2AccessToken, oAuth2Authentication);
                            accessTokens[0] = oAuth2AccessToken;
                        }
                );
        return accessTokens[0];
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username) {
        return accessTokenRepository
                .findByClientIdAndUsername(clientId, username)
                .stream()
                .map(AccessToken::getToken)
                .map(this::deserializeAccessToken)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clinetId) {
        return accessTokenRepository
                .findByClientId(clinetId)
                .stream()
                .map(AccessToken::getToken)
                .map(this::deserializeAccessToken)
                .collect(Collectors.toList());
    }

    protected byte[] serializeAccessToken(final OAuth2AccessToken token) {
        return serialize(token);
    }

    protected byte[] serializeRefreshToken(final OAuth2RefreshToken token) {
        return serialize(token);
    }

    protected byte[] serializeAuthentication(final OAuth2Authentication authentication) {
        return serialize(authentication);
    }

    protected OAuth2AccessToken deserializeAccessToken(final byte[] token) {
        return deserialize(token);
    }

    protected OAuth2RefreshToken deserializeRefreshToken(final byte[] token) {
        return deserialize(token);
    }

    protected OAuth2Authentication deserializeAuthentication(final byte[] authentication) {
        return deserialize(authentication);
    }

    private void removeRefreshToken(final String token) {
        final String tokenId = extractTokenKey(token);
        refreshTokenRepository.deleteById(tokenId);
    }

    private void removeAccessTokenUsingRefreshToken(final String refreshToken) {
        final String refreshTokenId = extractTokenKey(refreshToken);
        accessTokenRepository.deleteByRefreshTokenId(refreshTokenId);
    }

    private void removeAccessToken(final String tokenValue) {
        final String tokenKey = extractTokenKey(tokenValue);
        accessTokenRepository.deleteById(tokenKey);
    }

    private String extractTokenKey(String value) {
        if (value == null) {
            return null;
        }

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var5) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        byte[] e = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        return String.format("%032x", new BigInteger(1, e));
    }
}
