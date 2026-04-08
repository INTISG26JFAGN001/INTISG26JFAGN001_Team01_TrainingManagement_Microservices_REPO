package com.cognizant.tms.api_gateway.config;

import com.cognizant.tms.api_gateway.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebConfig {
    @Autowired
    private JwtFilter jwtAuthenticationFilter;

    @Bean
    public UserDetailsService userDetailsService(){
        return new InMemoryUserDetailsManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        httpSecurity.authorizeHttpRequests(auth->{
            auth.requestMatchers("/auth/**").permitAll();
            auth.requestMatchers("/user/all").hasRole("ADMIN");
            auth.anyRequest().permitAll();
        });
        httpSecurity.csrf(csrf->csrf.disable());
        httpSecurity.formLogin(form->form.disable());
        httpSecurity.httpBasic(httpBasic->httpBasic.disable());
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
