package com.basumatarau.samples.oauth.authprovider.repository;

import com.basumatarau.samples.oauth.authprovider.model.client.CustomClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientDetailsRepository extends JpaRepository<CustomClientDetails, String> {

    Optional<CustomClientDetails> findByClientId(String clientId);

    @Modifying
    @Query(value = "update CustomClientDetails details " +
            "set details.clientSecret=:newSecret " +
            "where details.clientId=:clientId")
    int updateClientSecret(@Param("clientId") String clientId,
                               @Param("newSecret") String newSecret);

    @Modifying
    @Query(value = "delete CustomClientDetails details " +
            "where details.clientId=:clientId")
    int deleteByClientId(@Param("clientId") String clientId);
}
