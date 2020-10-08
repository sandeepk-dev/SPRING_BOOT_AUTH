package com.sample.auth.filter;

import com.sample.auth.AppMain;
import com.sample.auth.annotations.Authorization;
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
public class AuthenticationAndAuthorizationFilter implements Filter {
    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String requestURI = httpRequest.getRequestURI();
        String api = requestURI.substring(requestURI.lastIndexOf('/') + 1);
        if (!AppMain.IGNORE_REQUEST_FILTERS.contains(api)) {
            UserDetails userDetails;
            try {
                userDetails = new UserAuthenticator(httpRequest).authenticate();
                LOGGER.info("Authentication successful for user : {} " , userDetails.getUserName());

            } catch (UserAuthenticationFailed e) {
                LOGGER.error("Authentication failed for user : " , e);
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            String methodName = AppMain.REQUEST_TO_METHOD_MAPPER.get(api);
            if (methodName == null) {
                LOGGER.error("No resource mappings found for api {}" , api);
                httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            try {
                Method method = ApiController.class.getMethod(methodName);
                Authorization authorization = method.getAnnotation(Authorization.class);

                if (authorization == null) {
                    LOGGER.error("No Authorization policies are associated with API");
                    httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                }
                if (authorization.enabled()) {
                    AuthorizeEntity authorizeEntity = new AuthorizeEntity.AuthorizeEntityBuilder().
                            setMiniMumRequiredRole(authorization.requiredRole()).
                            setUserRoles(userDetails.getUserRoles()).build();
                    if (new UserAuthorizeImpl(authorizeEntity).authorize()) {
                        LOGGER.info("Authorization successful for user : {} " , userDetails.getUserName());
                    }
                } else {
                    LOGGER.warn("Authorization is not enabled for resource : {}" , api);
                }
            } catch (UserAuthorizationFailed e) {
                LOGGER.error("Authorization failed for user : " + userDetails.getUserName() , e);
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } catch (NoSuchMethodException e) {
                LOGGER.error("Authorization failed for user : " + userDetails.getUserName() , e);
                httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

