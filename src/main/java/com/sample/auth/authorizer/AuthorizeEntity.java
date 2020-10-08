package com.sample.auth.authorizer;

import com.sample.auth.enums.UserRole;

import java.util.List;

public class AuthorizeEntity {

    private UserRole miniMumRequiredRole;
    private List<UserRole> userRoles;

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public UserRole getMiniMumRequiredRole() {
        return miniMumRequiredRole;
    }

    public AuthorizeEntity(AuthorizeEntityBuilder builder) {
        this.miniMumRequiredRole = builder.miniMumRequiredRole;
        this.userRoles = builder.userRoles;
    }

    public static class AuthorizeEntityBuilder {

        private UserRole miniMumRequiredRole;
        private List<UserRole> userRoles;

        public AuthorizeEntityBuilder setMiniMumRequiredRole(UserRole miniMumRequiredRole) {
            this.miniMumRequiredRole = miniMumRequiredRole;
            return this;
        }

        public AuthorizeEntityBuilder setUserRoles(List<UserRole> userRoles) {
            this.userRoles = userRoles;
            return this;
        }

        public AuthorizeEntity build() {
            return new AuthorizeEntity(this);
        }
    }
}

