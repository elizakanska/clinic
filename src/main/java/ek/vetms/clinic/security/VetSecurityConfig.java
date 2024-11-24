package ek.vetms.clinic.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class VetSecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                // Allow everyone to access static resources
                                .requestMatchers("/media/**", "/css/**", "/js/**", "/images/**").permitAll()

                                // Allow access to the homepage and 'About Us' page
                                .requestMatchers("/", "/par-mums").permitAll()

                                // Require authentication for all other requests
                                .anyRequest().authenticated()
                )
                .exceptionHandling(configurer -> configurer.accessDeniedPage("/access-denied"))
                .formLogin(form -> form
                        .loginProcessingUrl("/authenticate")  // The endpoint for form submission
                        .permitAll()  // Allow everyone to access the login processing
                        .successForwardUrl("/")  // Redirect to home after successful login
                )
                .logout(LogoutConfigurer::permitAll);

        http.csrf(AbstractHttpConfigurer::disable);  // Disable CSRF for simplicity; consider securing it later

        return http.build();
    }

}