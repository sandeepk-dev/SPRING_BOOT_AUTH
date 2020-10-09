package com.sample.auth.authorizer;

import com.sample.auth.enums.UserRole;
import com.sample.auth.exceptions.UserAuthorizationFailed;

public class UserAuthorizeImpl {

    private AuthorizationRequest authorizationRequest;

    public UserAuthorizeImpl(AuthorizationRequest authorizationRequest) {
        this.authorizationRequest = authorizationRequest;
    }

    public boolean authorize() throws UserAuthorizationFailed {

        // Implement your own authorization policies here (Sample implementation below)
        if (authorizationRequest.getUserRoles().contains(UserRole.ADMIN)) {
            return true;
        }

        //on failure throw UserAuthorizationFailed

        return false;
    }
}
