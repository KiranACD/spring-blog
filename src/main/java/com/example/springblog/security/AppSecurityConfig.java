package com.example.springblog.security;

import com.example.springblog.security.authToken.AuthTokenAuthenticationFilter;
import com.example.springblog.security.jwt.JWTAuthenticationFilter;
import com.example.springblog.security.jwt.JWTService;
import com.example.springblog.security.authToken.AuthTokenService;
import com.example.springblog.users.UsersService;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
    
    private final JWTService jwtService;
    private final AuthTokenService authTokenService;
    private final UsersService usersService;

    public AppSecurityConfig(
                    JWTService jwtService,
                    AuthTokenService authTokenService,
                    UsersService usersService
    ) {
        this.jwtService = jwtService;
        this.authTokenService = authTokenService;
        this.usersService = usersService;
    }

    protected void configure(HttpSecurity http) throws Exception {

        // TODO: In production setup these should not be deprecated
        http.csrf().disable().cors().disable();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users/**").permitAll()
                .antMatchers(HttpMethod.GET, "/articles/**").permitAll()
                .antMatchers(HttpMethod.GET, "/article/**").permitAll()
                .anyRequest().authenticated();
        
        http.addFilterBefore(new JWTAuthenticationFilter(jwtService), AnonymousAuthenticationFilter.class);
        http.addFilterBefore(new AuthTokenAuthenticationFilter(authTokenService), AnonymousAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
