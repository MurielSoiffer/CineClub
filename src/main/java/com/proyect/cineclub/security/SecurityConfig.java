package com.proyect.cineclub.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,  "/api/salas/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/peliculas/**", "/api/salas/**", "/api/funciones/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/peliculas/**", "/api/salas/**", "/api/funciones/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/peliculas/**", "/api/salas/**", "/api/funciones/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/usuarios/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/api/peliculas/**").permitAll()

                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .successHandler(successHandler()) //URL donde se va a ir despues de iniciar sesion
                        .permitAll())
                .sessionManagement(session ->session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) //ALWAYS - IF_REQUIRES - NEVER - STATELESS
                        .invalidSessionUrl("/login")
                        .maximumSessions(1)
                        .expiredUrl("/login")
                        .sessionRegistry(sessionRegistry())
                )
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession() //migrateSession - newSession - none
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    private AuthenticationSuccessHandler successHandler() {
        return(((request, response, authentication) ->
                response.sendRedirect("/api/funciones")));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
}
