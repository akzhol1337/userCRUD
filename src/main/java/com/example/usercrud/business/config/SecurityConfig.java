package com.example.usercrud.business.config;

import com.example.usercrud.business.entity.Permission;
import com.example.usercrud.business.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/createuser").hasAuthority(Permission.CREATE_USER.getPermission())
            .antMatchers(HttpMethod.POST, "/user").hasAuthority(Permission.CREATE_USER.getPermission())
            .antMatchers(HttpMethod.GET, "/user/id/*", "/users/email/*", "/users", "users/*", "/users/*/*", "/users/*/*/*").hasAuthority(Permission.GET_USER.getPermission())
            .antMatchers(HttpMethod.PUT, "/user/id/*", "/user/email/*").hasAuthority(Permission.EDIT_USER.getPermission())
            .antMatchers(HttpMethod.DELETE, "/user/id/*", "/user/email/*").hasAuthority(Permission.DELETE_USER.getPermission())
            .anyRequest().authenticated()
            .and()
            .httpBasic();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .authorities(Role.ADMIN.getAuthorities())
                .build(),
            User.builder()
                .username("user")
                .password(passwordEncoder().encode("user"))
                .authorities(Role.USER.getAuthorities())
                .build()
        );
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
