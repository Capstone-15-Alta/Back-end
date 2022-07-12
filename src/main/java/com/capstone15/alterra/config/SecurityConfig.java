package com.capstone15.alterra.config;

import com.capstone15.alterra.config.security.JwtTokenProvider;
import com.capstone15.alterra.config.security.SecurityFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfiguration {

    private final SecurityFilter securityFilter;

    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenProvider tokenProvider) throws Exception {
        http.httpBasic().and().cors().and().csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/v1/auth/**").permitAll()
                .antMatchers("/v1/user/**").permitAll()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.GET, "/v1/thread/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/category/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/comment/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/sub_comment/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/follow/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/like/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/save/**").permitAll()
                .antMatchers(HttpMethod.GET, "/images/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/report_thread/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/report_comment/**").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/report_thread/**").hasAuthority("MODERATOR")
                .antMatchers(HttpMethod.DELETE, "/v1/report_thread/**").hasAuthority("MODERATOR")
                .antMatchers(HttpMethod.PUT, "/v1/report_thread/**").hasAuthority("MODERATOR")
                .antMatchers(HttpMethod.POST, "/v1/report_comment/**").hasAuthority("MODERATOR")
                .antMatchers(HttpMethod.DELETE, "/v1/report_comment/**").hasAuthority("MODERATOR")
                .antMatchers(HttpMethod.PUT, "/v1/report_comment/**").hasAuthority("MODERATOR")
                .antMatchers(HttpMethod.DELETE, "/v1/category").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/v1/category").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/v1/category").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/v1/admin/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/v1/admin/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/v1/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Value("#{'${app.allowed-origins:}'.split(',')}")
            private List<String> allowedOrigins;

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                log.info("Allowed origin list: {}", allowedOrigins);
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins.toArray(new String[0]))
                        .allowedHeaders("X-Requested-With", "Origin", "Content-Type", "Accept", "Credential", "Authorization", "X-XSRF-TOKEN")
                        .allowedMethods("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH")
                        .maxAge(3600L)
                        .allowCredentials(true);
            }
        };
    }

}
