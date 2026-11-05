package com.javanauta.bffagendadortarefas.infrastructure.security;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SecurityScheme(name = SecurityConfig.SECURITY_SCHEME, scheme = "bearer", type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT")
public class SecurityConfig {

    public static final String SECURITY_SCHEME = "bearerAuth";
}
