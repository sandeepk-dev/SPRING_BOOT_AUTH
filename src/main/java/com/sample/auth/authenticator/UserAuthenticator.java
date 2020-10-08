package com.sample.auth.authenticator;

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
        // call respective service for user authentication
        //Success -> Construct UserDetails and return
        //Failure -> throw UserAuthenticationFailed exception
        return null;
    }
}
