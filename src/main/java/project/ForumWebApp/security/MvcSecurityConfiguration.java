package project.ForumWebApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import project.ForumWebApp.config.CustomAuthenticationEntryPoint;
import project.ForumWebApp.services.contracts.UserService;

@Configuration
@EnableWebSecurity
@Order(1)
public class MvcSecurityConfiguration {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public MvcSecurityConfiguration(
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint, UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean("mvcAuthenticationManager")
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
        return builder.build();
    }

    @Bean
    public SecurityFilterChain mvcSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/swagger-ui/**", "/v3/api-docs/**","/api/**"))
                .authorizeHttpRequests(auth -> {

                    auth.requestMatchers("/login", "/register").permitAll();
                    auth.requestMatchers("/api/**").permitAll();

                    auth.anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults())

                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

        return http.build();
    }
}
