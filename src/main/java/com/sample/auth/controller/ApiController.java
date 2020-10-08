package com.sample.auth.controller;

import com.sample.auth.annotations.Authorization;
import com.sample.auth.enums.UserRole;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/baseUri")
public class ApiController {

    @GetMapping("/getApi")
    @Authorization(enabled = true, requiredRole = UserRole.SUPPORT)
    public String getApiExample() {
        return "Successfully invoked getApi!";
    }

    @PostMapping("/postApi")
    @Authorization(enabled = true, requiredRole = UserRole.DEVELOPER)
    public String postApiExample() {
        return "Successfully invoked postApi!";
    }

    @DeleteMapping("/deleteApi")
    @Authorization(enabled = true, requiredRole = UserRole.ADMIN)
    public String deleteApiExample() {
        return "Successfully invoked deleteApi!";
    }

    @PutMapping("/putApi")
    @Authorization(enabled = true, requiredRole = UserRole.MAINTAINER)
    public String putApiExample() {
        return "Successfully invoked putApi!";
    }
}
