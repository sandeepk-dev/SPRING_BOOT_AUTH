package com.sample.auth.authorizer;

import com.sample.auth.enums.UserRole;
import com.sample.auth.exceptions.UserAuthorizationFailed;

public class UserAuthorizeImpl {

    private AuthorizeEntity authorizeEntity;

    public UserAuthorizeImpl(AuthorizeEntity authorizeEntity) {
        this.authorizeEntity = authorizeEntity;
    }

    public boolean authorize() throws UserAuthorizationFailed {

        // Implement your own authorization policies here (Sample implementation below)
        if (authorizeEntity.getUserRoles().contains(UserRole.ADMIN)) {
            return true;
        }


        return false;
    }
}
