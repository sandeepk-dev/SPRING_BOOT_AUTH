package com.sample.auth.authenticator;

import com.sample.auth.enums.AuthenticationType;

@SuppressWarnings("unchecked")
public class AuthenticationFactory <T extends Authenticator> {

    public T getAuthenticator(AuthenticationType type) {
        if (type == AuthenticationType.PASSWORD_BASED) {
            return (T) new PasswordAuthenticator();
        } else if(type == AuthenticationType.TOKEN_BASED) {
            return (T) new TokenAuthenticator();
        }
        return getDefaultAuthenticator();
    }

    public T getDefaultAuthenticator() {
        return (T) new PasswordAuthenticator();
    }

}
