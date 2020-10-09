package com.sample.auth;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.sample.auth.controller.ApiController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class AppMain {

    public static ImmutableMap<String, MethodNameToParameterMapper> REQUEST_TO_METHOD_MAPPER;

    public static final ImmutableList<Class<?>> CONTROLLER_LIST = ImmutableList.of(ApiController.class);

    public static final ImmutableSet<String> IGNORE_REQUEST_FILTERS = ImmutableSet.of("favicon.ico");

    public static void main(String[] args) {
        Map<String,MethodNameToParameterMapper> map = new HashMap<>();
        CONTROLLER_LIST.forEach(controller -> {
            for (Method method: controller.getMethods()) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                if (getMapping != null) {
                    String apiUrl = getMapping.value()[0];
                    updateUriToMethodMapping(map, method, apiUrl);
                    continue;
                }
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                if (postMapping != null) {
                    String apiUrl = postMapping.value()[0];
                    updateUriToMethodMapping(map, method, apiUrl);
                    continue;
                }
                DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                if (deleteMapping != null) {
                    String apiUrl = deleteMapping.value()[0];
                    updateUriToMethodMapping(map, method, apiUrl);
                    continue;
                }
                PutMapping putMapping = method.getAnnotation(PutMapping.class);
                if (putMapping != null) {
                    String apiUrl = putMapping.value()[0];
                    updateUriToMethodMapping(map, method, apiUrl);
                }
            }
        });
        REQUEST_TO_METHOD_MAPPER = ImmutableMap.copyOf(map);
        SpringApplication.run(AppMain.class, args);
    }

    private static void updateUriToMethodMapping(Map<String, MethodNameToParameterMapper> map, Method method, String apiUrl) {
        apiUrl = apiUrl.substring(apiUrl.lastIndexOf('/') + 1);
        map.put(apiUrl, new MethodNameToParameterMapper(method.getName(), method.getParameterTypes()));
    }

    public static class MethodNameToParameterMapper {
        private String methodName;
        private Class<?>[] parameterTypes;

        public String getMethodName() {
            return methodName;
        }

        public Class<?>[] getParameterTypes() {
            return parameterTypes;
        }

        public MethodNameToParameterMapper(String methodName, Class<?>[] parameterTypes) {
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;

        }
    }
}
