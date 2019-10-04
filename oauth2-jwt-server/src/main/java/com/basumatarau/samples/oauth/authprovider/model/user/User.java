package com.basumatarau.samples.oauth.authprovider.model.user;

import lombok.*;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = Identity.TYPE_USER)
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends Person{

    @EqualsAndHashCode.Exclude
    private Boolean enabled;

    @EqualsAndHashCode.Exclude
    private Boolean accountNonLocked;

    @EqualsAndHashCode.Exclude
    private Boolean accountNonExpired;

    @EqualsAndHashCode.Exclude
    private Boolean credentialsNonExpired;

    private Boolean accountConfirmed;

    private String email;

    @EqualsAndHashCode.Exclude
    private Long lastActive;

    @EqualsAndHashCode.Exclude
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role{
        USER, ADMIN
    }

    @Builder(builderMethodName = "userBuilder")
    public User(String name,
                String givenName,
                String familyName,
                Role role,
                Boolean enabled,
                Boolean accountNonLocked,
                Boolean accountNonExpired,
                Boolean credentialsNonExpired,
                Boolean accountConfirmed,
                String email,
                Long lastActive,
                String passwordHash) {
        super(name, givenName, familyName);
        this.role = role;
        this.enabled = enabled;
        this.accountNonLocked = accountNonLocked;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountConfirmed = accountConfirmed;
        this.email = email;
        this.lastActive = lastActive;
        this.passwordHash = passwordHash;
    }

    @PrePersist
    private void setDefaultRole(){
        if (role == null){
            role = Role.USER;
        }
    }
}
