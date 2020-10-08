package com.sample.auth.authorizer;

import com.sample.auth.exceptions.UserAuthorizationFailed;

public class UserAuthorizeImpl {

    private AuthorizeEntity authorizeEntity;

    public UserAuthorizeImpl(AuthorizeEntity authorizeEntity) {
        this.authorizeEntity = authorizeEntity;
    }

    public boolean authorize() throws UserAuthorizationFailed {
        // Implement you own authorization policies here (Sample implementation below)
        if (authorizeEntity.getUserRoles().contains(authorizeEntity.getMiniMumRequiredRole())) {
            throw new UserAuthorizationFailed(" User does not have proper roles");
        }
        return true;
    }
}
