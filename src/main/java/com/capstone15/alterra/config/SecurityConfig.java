package com.capstone15.alterra.config;

import com.capstone15.alterra.config.security.SecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/v1/auth/**").permitAll()
                .antMatchers("/v1/user/**").permitAll()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.GET, "/v1/thread/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/category/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/comment/**").permitAll()
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
                .anyRequest().authenticated();
        // remove session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // filter jwt
        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
