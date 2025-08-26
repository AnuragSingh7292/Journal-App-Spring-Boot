package com.example.JournalApp.config;

import com.example.JournalApp.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("Dev")
public class springSecurityDev {

    @Value("${spring.security.remember-me.key}")
    private String rememberMeKey;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**", "/login", "/signup").permitAll()
                        .requestMatchers("/Journal/**", "/user/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("Admin")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .loginProcessingUrl("/login") // Spring Security handles POST /login
                        .defaultSuccessUrl("/dashboard", true) // Redirect after success
                        .failureUrl("/login?error=true") // Redirect on failure
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .key(rememberMeKey) // Strong key for token hashing
                        .tokenValiditySeconds(7 * 24 * 60 * 60) // 7 days
                        .rememberMeParameter("remember-me") // Matches checkbox name
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .deleteCookies("JSESSIONID", "remember-me")
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable) // Disable for dev
                .build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
