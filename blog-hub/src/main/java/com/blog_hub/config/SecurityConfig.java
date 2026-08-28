package com.blog_hub.config;

import com.blog_hub.security.handler.JwtAccessDeniedHandler;
import com.blog_hub.security.handler.JwtAuthenticationEntryPoint;
import com.blog_hub.security.jwt.JwtAuthenticationFilter;
import com.blog_hub.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        // PUBLIC
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/refresh"
                        ).permitAll()
                        // READ POSTS
                        // USER + ADMIN
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/posts",
                                "/api/posts/**",
                                "/api/users/*/posts",
                                "/api/users/me"
                        ).hasAnyRole("USER", "ADMIN")
                        // CREATE POST
                        // USER + ADMIN
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/posts"
                        ).hasAnyRole("USER", "ADMIN")
                        // UPDATE OWN POST
                        // USER + ADMIN
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/posts/**"
                        ).hasAnyRole("USER", "ADMIN")
                        // UPDATE OWN PROFILE
                        // USER + ADMIN
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/users/me"
                        ).hasAnyRole("USER", "ADMIN")
                        // DELETE POST
                        // USER + ADMIN
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/posts/**"
                        ).hasAnyRole("USER", "ADMIN")
                        // DELETE USERS
                        // ADMIN ONLY
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/users/**"
                        ).hasRole("ADMIN")
                        // EVERYTHING ELSE
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .formLogin(form -> form.disable())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(customUserDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();

    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
}
