package com.sample.auth.annotations;

import com.sample.auth.enums.AuthenticationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authentication {
    boolean enabled() default true;
    AuthenticationType type() default AuthenticationType.PASSWORD_BASED;
}
