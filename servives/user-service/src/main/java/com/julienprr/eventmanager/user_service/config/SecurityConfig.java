package com.julienprr.eventmanager.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Configuration
@EnableMethodSecurity // pour @PreAuthorize
public class SecurityConfig {

    // ⚠️ Adapte si tu utilises des client roles (resource_access[<clientId>].roles)
    private static final String REALM_ROLES_CLAIM = "realm_access";
    private static final String REALM_ROLES_KEY = "roles";
    // private static final String CLIENT_ID = "event-app-dev"; // si tu préfères lire resource_access

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/users/signup",
                                "/actuator/health",
                                "/actuator/info"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))
                );

        return http.build();
    }

    /**
     * Mappe les rôles Keycloak -> autorités Spring "ROLE_<NAME>"
     * Ici on lit realm_access.roles. Si tu préfères les client roles:
     *   var ra = (Map<String, Object>) jwt.getClaims().get("resource_access");
     *   var app = (Map<String, Object>) ra.get(CLIENT_ID);
     *   var roles = (Collection<String>) app.get("roles");
     */
    private JwtAuthenticationConverter jwtAuthConverter() {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthoritiesFromJwt);
        return converter;
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractAuthoritiesFromJwt(Jwt jwt) {
        var authorities = new ArrayList<GrantedAuthority>();

        var realmAccess = (Map<String, Object>) jwt.getClaims().get(REALM_ROLES_CLAIM);
        if (realmAccess != null) {
            var roles = (Collection<String>) realmAccess.get(REALM_ROLES_KEY);
            if (roles != null) {
                roles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
            }
        }

        // -- Variante client roles --
        // var resourceAccess = (Map<String, Object>) jwt.getClaims().get("resource_access");
        // if (resourceAccess != null && resourceAccess.get(CLIENT_ID) instanceof Map<?,?> app) {
        //     var roles = (Collection<String>) ((Map<?,?>) app).get("roles");
        //     if (roles != null) roles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
        // }

        return authorities;
    }
}
