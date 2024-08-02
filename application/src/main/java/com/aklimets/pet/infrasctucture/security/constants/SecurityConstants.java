package com.aklimets.pet.infrasctucture.security.constants;

public class SecurityConstants {

    public static String[] WHITE_LIST_URLS = {
            "/api/v1/common/security/**", // if something should not be authenticated and is not in the jwt filter patterns
                                          // it should be placed here to avoid 401 error by the framework
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
