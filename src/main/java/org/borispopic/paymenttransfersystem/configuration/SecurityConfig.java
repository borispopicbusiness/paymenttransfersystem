package org.borispopic.paymenttransfersystem.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/public/**").permitAll()
                        .requestMatchers("/api/v1/**").authenticated()
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers.frameOptions().disable())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                );

        return http.build();
    }

/*    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/public/**").permitAll()
                        .requestMatchers("/api/v1/**").authenticated()
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers.frameOptions().disable())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .opaqueToken(opaque -> opaque
                                .introspectionUri("https://oauth2.googleapis.com/tokeninfo")
                                .introspectionClientCredentials("YOUR_CLIENT_ID", "YOUR_CLIENT_SECRET")
                        )
                );

        return http.build();
*//*        // Disable CSRF for simplicity (adjust if needed)
            http.csrf(AbstractHttpConfigurer::disable)
                // Permit H2 console and public endpoints without auth
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/public/**").permitAll()
                        // Secure API endpoints, require authenticated JWT
                        .requestMatchers("/api/v1/**").authenticated()
                        // Permit all other requests
                        .anyRequest().permitAll()
                )
                // Allow H2 console frames
                .headers(headers -> headers.frameOptions().disable())
                // Configure as OAuth2 Resource Server expecting JWT tokens
                //.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
                //.oauth2Login(Customizer.withDefaults());
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));


        return http.build();*//*
    }*/
}

