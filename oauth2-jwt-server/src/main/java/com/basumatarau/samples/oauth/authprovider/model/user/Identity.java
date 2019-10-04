package com.basumatarau.samples.oauth.authprovider.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "users", schema = "authorization_server_schema")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@ToString
@DiscriminatorColumn(name = "type")
public class Identity {

    public static final String TYPE_PERSON = "person";
    public static final String TYPE_USER = "user";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "identity_generator")
    @SequenceGenerator(name = "identity_generator",
            sequenceName = "users_id_seq",
            schema = "authorization_server_schema",
            allocationSize = 10)
    @Column(nullable = false, updatable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uuid", nullable = false)
    private ExternalId externalId;

    @PrePersist
    private void initExternalId(){
        if(externalId == null){
            externalId = new ExternalId();
        }
    }
}
