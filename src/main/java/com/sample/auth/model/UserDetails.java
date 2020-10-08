package com.sample.auth.model;

import com.sample.auth.enums.UserRole;

import java.util.List;

public class UserDetails {

    private String userName;

    public String getUserName() {
        return userName;
    }

    private List<UserRole> userRoles;

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public UserDetails(UserDetailsBuilder builder) {
        this.userName = builder.userName;
        this.userRoles = builder.userRoles;
    }

    public static class UserDetailsBuilder {
        public String userName;
        private List<UserRole> userRoles;

        public UserDetailsBuilder setUserRoles(List<UserRole> userRoles) {
            this.userRoles = userRoles;
            return this;
        }

        public UserDetailsBuilder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserDetails build() {
           return new  UserDetails(this);
        }
    }

}
