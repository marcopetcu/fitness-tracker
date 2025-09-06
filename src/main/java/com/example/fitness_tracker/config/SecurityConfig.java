package com.example.fitness_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**", "/h2-console/**").permitAll()
                .requestMatchers("/food/**").authenticated()
                .requestMatchers("/journal/**").authenticated()
                .requestMatchers("/workouts/**").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
            .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll())
            .headers(h -> h.frameOptions(f -> f.sameOrigin()))
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        return http.build();
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager mgr = new JdbcUserDetailsManager(dataSource);

        // autentificare pe email (1 utilizator = 1 rol)
        mgr.setUsersByUsernameQuery("""
            select email as username, password, enabled
            from app_user
            where email=?
        """);

        // mapare autorități: ROLE_<nume_rol>
        mgr.setAuthoritiesByUsernameQuery("""
            select u.email as username, concat('ROLE_', r.name) as authority
            from app_user u
            join role r on r.id = u.role_id
            where u.email=?
        """);

        return mgr;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
