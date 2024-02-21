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
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/api/v3/pets/list").hasRole("OWNER")
                        .requestMatchers("/api/v3/pets/addForm/**").hasRole("VET")
                        .requestMatchers("/api/v3/pets/editForm/**").hasRole("VET")
                        .requestMatchers("/api/v3/pets/delete/**").hasRole("VET")

                        .requestMatchers("/api/v3/visits/list").hasRole("OWNER")
                        .requestMatchers("/api/v3/visits/addForm/**").hasRole("VET")
                        .requestMatchers("/api/v3/visits/editForm/**").hasRole("VET")
                        .requestMatchers("/api/v3/visits/delete/**").hasRole("VET")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(configurer ->
                        configurer.
                                accessDeniedPage("/access-denied")
                )
                .formLogin(form ->
                        form
                                .loginPage("/loginPage")
                                .loginProcessingUrl("/authenticate")
                                .permitAll()
                )
                .logout(LogoutConfigurer::permitAll
                );

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
