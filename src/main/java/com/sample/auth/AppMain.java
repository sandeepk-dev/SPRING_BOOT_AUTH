package com.sample.auth;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppMain {

    //TODO register this via reflection on application startup
    public static final ImmutableMap<String, String> REQUEST_TO_METHOD_MAPPER
            = ImmutableMap.of("getApi", "getApiExample",
            "postApi", "postApiExample",
            "deleteApi", "deleteApiExample",
            "putApi", "putApiExample");

    public static final ImmutableSet<String> IGNORE_REQUEST_FILTERS = ImmutableSet.of("favicon.ico");

    public static void main(String[] args) {
        SpringApplication.run(AppMain.class, args);

    }
}
