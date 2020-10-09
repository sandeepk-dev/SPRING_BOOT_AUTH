package com.sample.auth.authorizer;

import com.sample.auth.enums.UserRole;

import java.util.List;

public class AuthorizationRequest {

    private UserRole miniMumRequiredRole;
    private List<UserRole> userRoles;

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public UserRole getMiniMumRequiredRole() {
        return miniMumRequiredRole;
    }

    public AuthorizationRequest(AuthorizationRequestBuilder builder) {
        this.miniMumRequiredRole = builder.miniMumRequiredRole;
        this.userRoles = builder.userRoles;
    }

    public static class AuthorizationRequestBuilder {

        private UserRole miniMumRequiredRole;
        private List<UserRole> userRoles;

        public AuthorizationRequestBuilder miniMumRequiredRole(UserRole miniMumRequiredRole) {
            this.miniMumRequiredRole = miniMumRequiredRole;
            return this;
        }

        public AuthorizationRequestBuilder userRoles(List<UserRole> userRoles) {
            this.userRoles = userRoles;
            return this;
        }

        public AuthorizationRequest build() {
            return new AuthorizationRequest(this);
        }
    }
}

