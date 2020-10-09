package com.sample.auth.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MutableHttpServletRequest extends HttpServletRequestWrapper {

    private final Map<String, Object> customHeaders;

    public MutableHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.customHeaders = new HashMap<>();
    }

    public void putCustomHeader(String header, Object value) {
        this.customHeaders.put(header, value);
    }

    public Object getCustomHeader(String header) {
        return customHeaders.getOrDefault(header, "HEADER DOES NOT EXISTS");
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> customHeaderName = new HashSet<>(customHeaders.keySet());
        Enumeration<String> headerNames = super.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            customHeaderName.add(headerName);
        }
        return Collections.enumeration(customHeaderName);
    }
}
