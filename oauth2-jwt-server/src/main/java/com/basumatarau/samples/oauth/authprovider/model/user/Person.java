package com.basumatarau.samples.oauth.authprovider.model.user;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = Identity.TYPE_PERSON)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class Person extends Identity{
    private String name;
    private String givenName;
    private String familyName;
}
