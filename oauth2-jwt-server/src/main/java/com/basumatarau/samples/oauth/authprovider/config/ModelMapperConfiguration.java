package com.basumatarau.samples.oauth.authprovider.config;

import com.basumatarau.samples.oauth.authprovider.converter.NewUserAccountToUser;
import com.basumatarau.samples.oauth.authprovider.converter.UserToUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(new UserToUserDetails());
        modelMapper.addConverter(getNewUserAccountToUserConverter());
        return modelMapper;
    }

    @Bean
    public NewUserAccountToUser getNewUserAccountToUserConverter(){
        return new NewUserAccountToUser();
    }

}
