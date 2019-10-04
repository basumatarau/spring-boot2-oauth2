package com.basumatarau.samples.oauth.authprovider.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@ToString
@Table(name = "external_ids", schema = "authorization_server_schema")
public class ExternalId {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private java.util.UUID uuid;
}
