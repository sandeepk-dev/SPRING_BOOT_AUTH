package com.sample.auth.controller;

import com.sample.auth.annotations.Authentication;
import com.sample.auth.annotations.Authorization;
import com.sample.auth.enums.AuthenticationType;
import com.sample.auth.enums.UserRole;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/baseUri")
public class ApiController {

    @GetMapping(value = "/getApi", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    @Authorization(requiredRole = UserRole.SUPPORT)
    @Authentication(type = AuthenticationType.PASSWORD_BASED)
    public String getApiExample() {
        return "Successfully invoked getApi!";
    }

    @PostMapping(value = "/postApi", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    @Authorization(enabled = false, requiredRole = UserRole.DEVELOPER)
    @Authentication(type = AuthenticationType.TOKEN_BASED)
    public String postApiExample(@RequestBody String body) {
        return "Successfully invoked postApi!";
    }

    @DeleteMapping(value = "/deleteApi", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    @Authorization(enabled = false, requiredRole = UserRole.ADMIN)
    @Authentication(type = AuthenticationType.TOKEN_BASED)
    public String deleteApiExample(@RequestBody String id) {
        return "Successfully invoked deleteApi!";
    }

    @PutMapping(value = "/putApi", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    @Authorization(enabled = true, requiredRole = UserRole.MAINTAINER)
    @Authentication(type = AuthenticationType.PASSWORD_BASED)
    public String putApiExample(@RequestBody String body) {
        return "Successfully invoked putApi!";
    }
}
