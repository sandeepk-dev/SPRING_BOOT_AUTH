package com.sample.auth.authenticator;

import com.sample.auth.enums.AuthenticationType;

@SuppressWarnings("unchecked")
public class AuthenticationFactory {

    public static <T extends Authenticator> T getAuthenticator(AuthenticationType type) {
        if (type == AuthenticationType.PASSWORD_BASED) {
            return (T) new PasswordAuthenticator();
        } else if(type == AuthenticationType.TOKEN_BASED) {
            return (T) new TokenAuthenticator();
        }
        return getDefaultAuthenticator();
    }

    public static <T extends Authenticator> T getDefaultAuthenticator() {
        return (T) new PasswordAuthenticator();
    }

}
