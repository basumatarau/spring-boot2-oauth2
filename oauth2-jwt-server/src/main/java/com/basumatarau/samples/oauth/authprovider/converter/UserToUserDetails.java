package com.basumatarau.samples.oauth.authprovider.converter;

import com.basumatarau.samples.oauth.authprovider.model.user.User;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.security.core.userdetails.UserDetails;

public class UserToUserDetails implements Converter<User, UserDetails> {

    @Override
    public UserDetails convert(MappingContext<User, UserDetails> mappingContext) {
        return mappingContext.getSource() == null ? null :
                org.springframework.security.core.userdetails.User.builder()
                        .password(mappingContext.getSource().getPasswordHash())
                        .roles(mappingContext.getSource().getRole().name())
                        .disabled(!mappingContext.getSource().getEnabled())
                        .accountExpired(!mappingContext.getSource().getAccountNonExpired())
                        .accountLocked(!mappingContext.getSource().getAccountNonLocked())
                        .credentialsExpired(!mappingContext.getSource().getCredentialsNonExpired())
                        .username(mappingContext.getSource().getEmail())
                        .build();
    }
}
