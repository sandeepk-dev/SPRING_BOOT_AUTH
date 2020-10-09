package com.sample.auth.authenticator;

import com.google.common.collect.ImmutableList;
import com.sample.auth.enums.UserRole;
import com.sample.auth.exceptions.UserAuthenticationFailed;
import com.sample.auth.model.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordAuthenticator implements Authenticator {
    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDetails authenticate(AuthenticationRequest request) throws UserAuthenticationFailed {
        UserDetails userDetails;
        try {
//            String userName =  request.getUserName();
//            String password = request.getPassword();
            /* Authenticate user here and return UserDetails. Upon failure throw UserAuthorizationFailed */

            /*Dummy userDetails*/
            userDetails = new UserDetails.UserDetailsBuilder()
                    .userName("USER")
                    .userRoles(ImmutableList.of(UserRole.ADMIN))
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error authenticating user ", e);
            throw new UserAuthenticationFailed(e);
        }
        return userDetails;
    }
}
