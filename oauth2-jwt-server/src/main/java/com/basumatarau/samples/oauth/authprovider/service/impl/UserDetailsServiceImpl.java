package com.basumatarau.samples.oauth.authprovider.service.impl;


import com.basumatarau.samples.oauth.authprovider.model.user.User;
import com.basumatarau.samples.oauth.authprovider.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        final User retrievedUser = userRepository.findByEmail(s)
                .orElseThrow(() -> new EntityNotFoundException("no user found by email: " + s));
        return convertToUserDetails(retrievedUser);
    }

    private UserDetails convertToUserDetails(User retrievedUser) {
        return modelMapper.map(retrievedUser, UserDetails.class);
    }
}
