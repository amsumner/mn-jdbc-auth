package com.practice.auth;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/secured")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class SecuredEndpoint {

    @Get("/status")
    public String status(){
        return "You are authenticated";
    }
}
