package com.sample.auth.authenticator;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class AuthenticationRequest {

    private Optional<String> token;
    private Optional<String> userName;
    private Optional<String> password;

    private Supplier<String> fieldSupplier = () -> {
        throw new RuntimeException("Field does not exists");
    };

    public String getPassword() {
        return password.orElseGet(fieldSupplier);
    }

    public String getToken() {
        return token.orElseGet(fieldSupplier);
    }

    public String getUserName() {
        return userName.orElseGet(fieldSupplier);
    }

    public AuthenticationRequest(AuthenticationRequestBuilder builder) {
        token = Optional.ofNullable(builder.token);
        userName = Optional.ofNullable(builder.userName);
        password = Optional.ofNullable(builder.password);
    }


    public static class AuthenticationRequestBuilder {
        private String token;
        private String userName;
        private String password;


        public AuthenticationRequestBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public AuthenticationRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AuthenticationRequestBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthenticationRequest build() {
            return new AuthenticationRequest(this);
        }
    }

}
