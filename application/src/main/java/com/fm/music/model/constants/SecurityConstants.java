package com.fm.music.model.constants;

public class SecurityConstants {

    public static String[] WHITE_LIST_URLS = {
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
