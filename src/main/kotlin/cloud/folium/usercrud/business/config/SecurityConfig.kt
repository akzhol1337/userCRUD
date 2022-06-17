package cloud.folium.usercrud.business.config

import kotlin.Throws
import cloud.folium.usercrud.business.entity.Permission
import cloud.folium.usercrud.business.entity.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.lang.Exception

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/createuser").hasAuthority(Permission.CREATE_USER.permission)
            .antMatchers(HttpMethod.POST, "/user").hasAuthority(Permission.CREATE_USER.permission)
            .antMatchers(
                HttpMethod.GET,
                "/user/id/*",
                "/users/email/*",
                "/users",
                "users/*",
                "/users/*/*",
                "/users/*/*/*"
            ).hasAuthority(
                Permission.GET_USER.permission
            )
            .antMatchers(HttpMethod.PUT, "/user/id/*", "/user/email/*")
            .hasAuthority(Permission.EDIT_USER.permission)
            .antMatchers(HttpMethod.DELETE, "/user/id/*", "/user/email/*")
            .hasAuthority(Permission.DELETE_USER.permission)
            .anyRequest().authenticated()
            .and()
            .httpBasic()
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/h2-console/**")
    }

    @Bean
    override fun userDetailsService(): UserDetailsService {
        return InMemoryUserDetailsManager(
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
        )
    }

    @Bean
    protected fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(12)
    }
}