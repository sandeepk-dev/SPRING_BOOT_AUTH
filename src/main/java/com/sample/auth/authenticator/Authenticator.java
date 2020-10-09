package com.sample.auth.authenticator;

import com.sample.auth.exceptions.UserAuthenticationFailed;
import com.sample.auth.model.UserDetails;

public interface Authenticator {
    default UserDetails authenticate(AuthenticationRequest request) throws UserAuthenticationFailed {
        throw new UserAuthenticationFailed("User Authentication Failed");
    }
}
