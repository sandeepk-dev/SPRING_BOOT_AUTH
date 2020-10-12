package com.sample.auth.filters;

import com.sample.auth.AppMain;
import com.sample.auth.annotations.Authorization;
import com.sample.auth.authorizer.AuthorizationRequest;
import com.sample.auth.authorizer.UserAuthorizeImpl;
import com.sample.auth.controller.ApiController;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

import static com.sample.auth.filters.AuthenticationFilter.METHOD_DETAILS;
import static com.sample.auth.filters.AuthenticationFilter.USER_DETAILS;

@Component
@Order(2)
public class AuthorizationFilter implements Filter {
    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        MutableHttpServletRequest httpRequest = (MutableHttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String requestURI = httpRequest.getRequestURI();
        String api = requestURI.substring(requestURI.lastIndexOf('/') + 1);
        if (!AppMain.IGNORE_REQUEST_FILTERS.contains(api)) {
            UserDetails userDetails = (UserDetails) httpRequest.getCustomHeader(USER_DETAILS);
            AppMain.MethodNameToParameterMapper methodDetails = (AppMain.MethodNameToParameterMapper) httpRequest.getCustomHeader(METHOD_DETAILS);
            try {
                if (authorizeUser(ApiController.class.getMethod(methodDetails.getMethodName(), methodDetails.getParameterTypes()), userDetails)) {
                    LOGGER.info("Authorization successful for user : {} ", userDetails.getUserName());
                }
            } catch (NoSuchMethodException e) {
                LOGGER.error("Authorization failed for user : " + userDetails.getUserName(), e);
                httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            } catch (UserAuthorizationFailed e) {
                LOGGER.error("Authorization failed for user : " + userDetails.getUserName(), e);
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(servletRequest, httpResponse);
    }

    private boolean authorizeUser(Method method, UserDetails userDetails) throws UserAuthorizationFailed {
        if (method.isAnnotationPresent(Authorization.class) && method.getAnnotation(Authorization.class).enabled()) {
            Authorization authorization = method.getAnnotation(Authorization.class);
            AuthorizationRequest authorizationRequest = new AuthorizationRequest.AuthorizationRequestBuilder().
                    miniMumRequiredRole(authorization.requiredRole())
                    .userRoles(userDetails.getUserRoles())
                    .build();
            return new UserAuthorizeImpl(authorizationRequest).authorize();
        } else {
            LOGGER.warn("Authorization is not enabled for resource");
        }
        return true;
    }
}
