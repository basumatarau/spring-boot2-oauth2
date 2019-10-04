package com.basumatarau.samples.oauth.authprovider.repository;

import com.basumatarau.samples.oauth.authprovider.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
}
