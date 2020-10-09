package com.sample.auth.authenticator;

import com.sample.auth.enums.AuthenticationType;

public class AuthenticationFactory <T extends Authenticator> {

    @SuppressWarnings("unchecked")
    public T getAuthenticator(AuthenticationType type) {
        if (type == AuthenticationType.PASSWORD_BASED) {
            return (T) new PasswordAuthenticator();
        } else if(type == AuthenticationType.TOKEN_BASED) {
            return (T) new TokenAuthenticator();
        }
        return null;
    }

}
