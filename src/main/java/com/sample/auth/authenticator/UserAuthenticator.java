package com.sample.auth.authenticator;

import com.sample.auth.exceptions.UserAuthenticationFailed;
import com.sample.auth.model.UserDetails;

public class UserAuthenticator<T extends Authenticator> {

    private AuthenticationRequest request;
    private T authenticator;

    public UserAuthenticator(AuthenticationRequest request, T authenticator) {
        this.request = request;
        this.authenticator = authenticator;
    }

    public UserDetails authenticate() throws UserAuthenticationFailed {
        return authenticator.authenticate(request);
    }
}
