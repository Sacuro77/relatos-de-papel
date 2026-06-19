package com.actividad2.cloud_gateway.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AccessTokenRequestWrapper extends HttpServletRequestWrapper {

    private final String accessTokenHeader;
    private final String accessToken;

    public AccessTokenRequestWrapper(HttpServletRequest request, String accessTokenHeader, String accessToken) {
        super(request);
        this.accessTokenHeader = accessTokenHeader;
        this.accessToken = accessToken;
    }

    @Override
    public String getHeader(String name) {
        if (accessTokenHeader.equalsIgnoreCase(name)) {
            return accessToken;
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (accessTokenHeader.equalsIgnoreCase(name)) {
            return Collections.enumeration(List.of(accessToken));
        }
        return super.getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> names = new LinkedHashSet<>();
        Enumeration<String> existingNames = super.getHeaderNames();
        while (existingNames.hasMoreElements()) {
            names.add(existingNames.nextElement());
        }

        boolean alreadyPresent = names.stream().anyMatch(accessTokenHeader::equalsIgnoreCase);
        if (!alreadyPresent) {
            names.add(accessTokenHeader);
        }

        return Collections.enumeration(new ArrayList<>(names));
    }
}
