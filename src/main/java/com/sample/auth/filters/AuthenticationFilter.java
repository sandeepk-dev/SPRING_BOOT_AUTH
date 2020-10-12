package com.sample.auth.filters;

import com.sample.auth.AppMain;
import com.sample.auth.annotations.Authentication;
import com.sample.auth.authenticator.AuthenticationFactory;
import com.sample.auth.authenticator.AuthenticationRequest;
import com.sample.auth.authenticator.Authenticator;
import com.sample.auth.authenticator.UserAuthenticatorImpl;
import com.sample.auth.exceptions.UserAuthenticationFailed;
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

import static com.sample.auth.AppMain.CONTROLLER_LIST;

@Component
@Order(1)
public class AuthenticationFilter<T extends Authenticator> implements Filter {
    Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static final String USER_NAME = "USER_NAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String AUTHENTICATION_TOKEN = "AUTHENTICATION_TOKEN";
    public static final String USER_DETAILS = "USER_DETAILS";
    public static final String METHOD_DETAILS = "METHOD_DETAILS";


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        MutableHttpServletRequest mutableHttpRequest = new MutableHttpServletRequest(httpRequest);
        String requestURI = httpRequest.getRequestURI();
        String api = requestURI.substring(requestURI.lastIndexOf('/') + 1);
        if (!AppMain.IGNORE_REQUEST_FILTERS.contains(api)) {
            AppMain.MethodNameToParameterMapper methodDetails = AppMain.REQUEST_TO_METHOD_MAPPER.get(api);
            if (methodDetails == null) {
                LOGGER.error("No resource mappings found for api {}", api);
                httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            try {
                for (Class<?> controller : CONTROLLER_LIST) {
                    Method method = controller.getMethod(methodDetails.getMethodName(), methodDetails.getParameterTypes());
                    UserDetails userDetails = null;
                    if (method.isAnnotationPresent(Authentication.class) && method.getAnnotation(Authentication.class).enabled()) {
                        Authentication authentication = method.getAnnotation(Authentication.class);
                        T authenticator = AuthenticationFactory.getAuthenticator(authentication.type());
                        userDetails = authenticateUser(httpRequest, authenticator);
                    }
                    if (userDetails != null) {
                        LOGGER.info("Authentication successful for user :  {}", userDetails.getUserName());
                        mutableHttpRequest.putCustomHeader(USER_DETAILS, userDetails);
                        mutableHttpRequest.putCustomHeader(METHOD_DETAILS, methodDetails);
                    } else {
                        LOGGER.info("Authentication not enabled for api : {}", api);
                    }
                }
            } catch (NoSuchMethodException e) {
                LOGGER.error("Authentication failed for user : ", e);
                httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            } catch (UserAuthenticationFailed e) {
                LOGGER.error("Authentication failed for user : ", e);
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        filterChain.doFilter(mutableHttpRequest, httpResponse);
    }

    private UserDetails authenticateUser(HttpServletRequest httpRequest, T authenticator) throws UserAuthenticationFailed {
        UserDetails userDetails;
        String userName = httpRequest.getHeader(USER_NAME);
        String password = httpRequest.getHeader(PASSWORD);
        String token = httpRequest.getHeader(AUTHENTICATION_TOKEN);
        AuthenticationRequest request = new AuthenticationRequest.AuthenticationRequestBuilder().userName(userName).password(password).token(token).build();
        userDetails = new UserAuthenticatorImpl<>(request, authenticator).authenticate();
        return userDetails;
    }


}

