package com.aklimets.pet.infrasctucture.security.constants;

public class SecurityConstants {

    public static String[] WHITE_LIST_URLS = {
            "/api/v1/common/security/authenticate",
            "/api/v1/common/security/refresh",
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**"
    };
}
