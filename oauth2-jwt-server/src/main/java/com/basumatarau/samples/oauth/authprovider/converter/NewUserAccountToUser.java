package com.basumatarau.samples.oauth.authprovider.converter;


import com.basumatarau.samples.oauth.authprovider.dto.NewAccountDto;
import com.basumatarau.samples.oauth.authprovider.model.user.User;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class NewUserAccountToUser implements Converter<NewAccountDto, User> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User convert(MappingContext<NewAccountDto, User> mappingContext) {
        if (mappingContext.getSource() == null){
            return null;
        }

        return User.userBuilder()
                .accountNonExpired(true)
                //todo fix confirmation
                .accountConfirmed(true)
                .accountNonLocked(true)
                //todo fix confirmation
                .enabled(true)
                .lastActive(new Date().toInstant().getEpochSecond())
                .role(User.Role.USER)
                .credentialsNonExpired(true)
                .email(mappingContext.getSource().getEmail())
                .passwordHash(passwordEncoder.encode(mappingContext.getSource().getRawPassword()))
                .build();
    }
}
