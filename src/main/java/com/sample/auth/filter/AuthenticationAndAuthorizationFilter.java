package com.sample.auth.filter;

import com.sample.auth.AppMain;
import com.sample.auth.annotations.Authentication;
import com.sample.auth.annotations.Authorization;
import com.sample.auth.authenticator.AuthenticationFactory;
import com.sample.auth.authenticator.AuthenticationRequest;
import com.sample.auth.authenticator.Authenticator;
import com.sample.auth.authenticator.UserAuthenticator;
import com.sample.auth.authorizer.AuthorizeEntity;
import com.sample.auth.authorizer.UserAuthorizeImpl;
import com.sample.auth.controller.ApiController;
import com.sample.auth.exceptions.UserAuthenticationFailed;
import com.sample.auth.exceptions.UserAuthorizationFailed;
import com.sample.auth.model.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@Component
@Order(1)
public class AuthenticationAndAuthorizationFilter<T extends Authenticator> implements Filter {
    Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static final String USER_NAME = "USER_NAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String AUTHENTICATION_TOKEN = "AUTHENTICATION_TOKEN";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String requestURI = httpRequest.getRequestURI();
        String api = requestURI.substring(requestURI.lastIndexOf('/') + 1);
        if (!AppMain.IGNORE_REQUEST_FILTERS.contains(api)) {
            String methodName = AppMain.REQUEST_TO_METHOD_MAPPER.get(api);
            if (methodName == null) {
                LOGGER.error("No resource mappings found for api {}" , api);
                httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            try {
                Method method = ApiController.class.getMethod(methodName);
                UserDetails userDetails;
                Authentication authentication = method.getAnnotation(Authentication.class);
                if (authentication != null && authentication.enabled()) {
                    T authenticator = new AuthenticationFactory<T>().getAuthenticator(authentication.type());
                    userDetails = authenticateUser(httpRequest, authenticator);
                    if (userDetails != null || !authorizeUser(httpResponse, api, method, userDetails)) {
                        return;
                    }
                } else {
                    LOGGER.info("Authentication not enabled for api : {}", api);
                }
            } catch (NoSuchMethodException e) {
                LOGGER.error("Authentication failed for user : ", e);
                httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            } catch (UserAuthenticationFailed e) {
                LOGGER.error("Authentication failed for user : " , e);
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private UserDetails authenticateUser(HttpServletRequest httpRequest, T authenticator) throws UserAuthenticationFailed {
        UserDetails userDetails;
        String userName = httpRequest.getHeader(USER_NAME);
        String password = httpRequest.getHeader(PASSWORD);
        String token = httpRequest.getHeader(AUTHENTICATION_TOKEN);
        AuthenticationRequest request = new AuthenticationRequest.AuthenticationRequestBuilder().userName(userName).password(password).token(token).build();
        userDetails = new UserAuthenticator<>(request, authenticator).authenticate();
        return userDetails;
    }

    private boolean authorizeUser(HttpServletResponse httpResponse, String api, Method method, UserDetails userDetails) throws IOException {
        try {
            Authorization authorization = method.getAnnotation(Authorization.class);
            if (authorization != null && authorization.enabled()) {
                AuthorizeEntity authorizeEntity = new AuthorizeEntity.AuthorizeEntityBuilder().
                        miniMumRequiredRole(authorization.requiredRole()).
                        userRoles(userDetails.getUserRoles()).build();
                if (new UserAuthorizeImpl(authorizeEntity).authorize()) {
                    LOGGER.info("Authorization successful for user : {} " , userDetails.getUserName());
                }
            }  else {
                LOGGER.warn("Authorization is not enabled for resource : {}" , api);
            }
        } catch (UserAuthorizationFailed e) {
            LOGGER.error("Authorization failed for user : " + userDetails.getUserName() , e);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}

