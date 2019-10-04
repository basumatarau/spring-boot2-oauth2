package com.basumatarau.samples.oauth.authprovider.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/oauth")
public class UserInfoController {

    //todo: enable the endpoint as oidc userInfo service (configure the resource server)
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Principal> get(final Principal principal) {
        return ResponseEntity.ok(principal);
    }

}
