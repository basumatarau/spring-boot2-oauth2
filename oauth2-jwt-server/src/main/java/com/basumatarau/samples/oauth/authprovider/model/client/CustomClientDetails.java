package com.basumatarau.samples.oauth.authprovider.model.client;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomClientDetails implements ClientDetails {

    @Id
    private String clientId;
    private String clientSecret;

    @EqualsAndHashCode.Exclude
    private Set<String> scope = new HashSet<>();


    //todo implement mapping for collections

    @EqualsAndHashCode.Exclude
    private Set<String> resourceIds = new HashSet<>();

    @EqualsAndHashCode.Exclude
    private Set<String> autoApproveScopes;

    @EqualsAndHashCode.Exclude
    private Set<String> authorizedGrantTypes = new HashSet<>();

    @EqualsAndHashCode.Exclude
    private Set<String> registeredRedirectUris = new HashSet<>();

    @EqualsAndHashCode.Exclude
    private Set<String> authorities = new HashSet<>();

    @EqualsAndHashCode.Exclude
    private Integer accessTokenValiditySeconds;

    @EqualsAndHashCode.Exclude
    private Integer refreshTokenValiditySeconds;

    @EqualsAndHashCode.Exclude
    @ElementCollection
    private Map<String, Object> additionalInformation = new HashMap<>();

    @Override
    public boolean isScoped() {
        return this.scope != null && !this.scope.isEmpty();
    }

    @Override
    public boolean isSecretRequired() {
        return this.clientSecret != null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities(){
        return this.authorities
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAutoApprove(final String scope) {
        if (Objects.isNull(autoApproveScopes)) {
            return false;
        }
        for (String auto : autoApproveScopes) {
            if ("true".equals(auto) || scope.matches(auto)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUris;
    }
}
