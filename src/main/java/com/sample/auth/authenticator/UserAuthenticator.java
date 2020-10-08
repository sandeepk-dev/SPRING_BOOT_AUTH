package com.sample.auth.authenticator;

import com.google.common.collect.ImmutableList;
import com.sample.auth.enums.UserRole;
import com.sample.auth.exceptions.UserAuthenticationFailed;
import com.sample.auth.model.UserDetails;

import javax.servlet.http.HttpServletRequest;

public class UserAuthenticator {

    private HttpServletRequest httpRequest;
    private static final String AUTHENTICATION_TOKEN = "AUTHENTICATION_TOKEN";

    public UserAuthenticator(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public UserDetails authenticate() throws UserAuthenticationFailed {
        httpRequest.getHeader(AUTHENTICATION_TOKEN);
        // call respective service for user authentication passing token
        //Success -> Construct UserDetails and return
        //Failure -> throw UserAuthenticationFailed exception

        /* Sample response*/
        return new UserDetails.UserDetailsBuilder()
                .setUserName("USER")
                .setUserRoles(ImmutableList.of(UserRole.ADMIN, UserRole.MAINTAINER, UserRole.SUPPORT, UserRole.DEVELOPER))
                .build();
    }
}
