package ek.vetms.clinic.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                        .requestMatchers(HttpMethod.GET, "/api/v2/pets").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/v2/pets/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.POST, "/api/v2/pets").hasRole("VET")
                        .requestMatchers(HttpMethod.PUT, "/api/v2/pets/**").hasRole("VET")
                        .requestMatchers(HttpMethod.DELETE, "/api/v2/pets/**").hasRole("VET")

                        .requestMatchers(HttpMethod.GET, "/api/v2/visits").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/v2/visits/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.POST, "/api/v2/visits").hasRole("VET")
                        .requestMatchers(HttpMethod.PUT, "/api/v2/visits/**").hasRole("VET")
                        .requestMatchers(HttpMethod.DELETE, "/api/v2/visits/**").hasRole("VET")
        );

        http.httpBasic(Customizer.withDefaults());

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
