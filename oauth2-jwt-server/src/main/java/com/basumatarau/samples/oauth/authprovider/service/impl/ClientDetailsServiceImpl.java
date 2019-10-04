package com.basumatarau.samples.oauth.authprovider.service.impl;

import com.basumatarau.samples.oauth.authprovider.model.client.CustomClientDetails;
import com.basumatarau.samples.oauth.authprovider.repository.ClientDetailsRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientDetailsServiceImpl implements ClientDetailsService, ClientRegistrationService {

    private final ClientDetailsRepository clientDetailsRepository;

    private final PasswordEncoder passwordEncoder;

    public ClientDetailsServiceImpl(ClientDetailsRepository clientDetailsRepository,
                                    PasswordEncoder passwordEncoder) {

        this.clientDetailsRepository = clientDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        return clientDetailsRepository.findByClientId(clientId)
                .orElseThrow(() -> new ClientRegistrationException("no client details found by client id: " + clientId));
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        clientDetailsRepository.save(
                buildCustomClientDetails(clientDetails)
        );
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        final Optional<CustomClientDetails> optClient = clientDetailsRepository
                .findByClientId(clientDetails.getClientId());

        if (optClient.isPresent()) {
            clientDetailsRepository.save(buildCustomClientDetails(optClient.get()));
        } else {
            throw new NoSuchClientException("no client found by client id: " + clientDetails.getClientId());
        }
    }

    @Override
    public void updateClientSecret(final String clientId,
                                   final String newSecret)
            throws NoSuchClientException {
        if(clientDetailsRepository.updateClientSecret(clientId, newSecret) == 0){
            throw new NoSuchClientException("no client for secret update found by id: " + clientId);
        }
    }

    @Override
    public void removeClientDetails(String clientId)
            throws NoSuchClientException {
        if(clientDetailsRepository.deleteByClientId(clientId) == 0){
            throw new NoSuchClientException("no client for secret update found by id: " + clientId);
        }
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        return clientDetailsRepository
                .findAll()
                .stream().map(this::buildCustomClientDetails)
                .collect(Collectors.toList());
    }

    private CustomClientDetails buildCustomClientDetails(ClientDetails clientDetails) {
        return CustomClientDetails.builder()
                .clientId(clientDetails.getClientId())
                .clientSecret(passwordEncoder.encode(clientDetails.getClientSecret()))
                .accessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds())
                .refreshTokenValiditySeconds(clientDetails.getRefreshTokenValiditySeconds())
                .additionalInformation(clientDetails.getAdditionalInformation())
                .authorities(
                        clientDetails.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toSet())
                )
                .registeredRedirectUris(clientDetails.getRegisteredRedirectUri())
                .scope(clientDetails.getScope())
                .resourceIds(clientDetails.getResourceIds())
                .authorizedGrantTypes(clientDetails.getAuthorizedGrantTypes())
                .additionalInformation(clientDetails.getAdditionalInformation())
                .autoApproveScopes(getAutoApproveScopes(clientDetails))
                .build();
    }

    private Set<String> getAutoApproveScopes(ClientDetails details) {
        if(details.isAutoApprove("true")){
            return Collections.singleton("true"); //all scopes are approved so the stub persisted
        }

        return details.getScope()
                .stream()
                .filter(details::isAutoApprove)
                .collect(Collectors.toSet());
    }

}
