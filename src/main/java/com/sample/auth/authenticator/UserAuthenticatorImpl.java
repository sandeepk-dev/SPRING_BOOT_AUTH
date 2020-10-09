package com.sample.auth.authenticator;

import com.sample.auth.exceptions.UserAuthenticationFailed;
import com.sample.auth.model.UserDetails;

public class UserAuthenticatorImpl<T extends Authenticator> {

    private AuthenticationRequest request;
    private T authenticator;

    public UserAuthenticatorImpl(AuthenticationRequest request, T authenticator) {
        this.request = request;
        this.authenticator = authenticator;
    }

    public UserDetails authenticate() throws UserAuthenticationFailed {
        return authenticator.authenticate(request);
    }
}
