package com.cognizant.tms.api_gateway.config;

import com.cognizant.tms.api_gateway.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(cors->cors.configurationSource(corsConfigurationSource()));
        httpSecurity
            .authorizeHttpRequests(auth->{
                auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                auth.requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/webjars/**"
                ).permitAll();
                // Auth Controller RBAC
                auth.requestMatchers(HttpMethod.POST, "/auth/signup").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                auth.requestMatchers(HttpMethod.POST, "/auth/refresh-token").permitAll();

                // User Controller RBAC
                auth.requestMatchers(HttpMethod.GET, "/user/all").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.GET, "/user/**").hasAnyRole("ADMIN", "TECH_LEAD", "TRAINER", "COACH");
                auth.requestMatchers(HttpMethod.PUT, "/user/**").hasAnyRole("ADMIN", "COACH");
                auth.requestMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN");

                // Associate Controller RBAC
                auth.requestMatchers(HttpMethod.POST, "/associates/create").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.PUT, "/associates/update").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.GET, "/associates/{userId}").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH","ASSOCIATE");
                auth.requestMatchers(HttpMethod.GET, "/associates").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH","ASSOCIATE");
                auth.requestMatchers(HttpMethod.GET, "/associates/batch").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH","ASSOCIATE");
                auth.requestMatchers(HttpMethod.GET, "/associates/xp").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH","ASSOCIATE");

                // Batches Controller
                auth.requestMatchers(HttpMethod.POST, "/batches").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.GET, "/batches").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH");
                auth.requestMatchers(HttpMethod.GET, "/batches/{id}").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH");
                auth.requestMatchers(HttpMethod.DELETE, "/batches/{id}").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.PUT, "/batches/{id}/status").hasAnyRole("ADMIN","TRAINER");
                auth.requestMatchers(HttpMethod.GET, "/batches/status").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH");
                auth.requestMatchers(HttpMethod.GET, "/batches/course").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH");
                auth.requestMatchers(HttpMethod.GET, "/batches/trainer").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH");
                auth.requestMatchers(HttpMethod.GET, "/batches/{id}/details").hasAnyRole("ADMIN","TRAINER");
                auth.requestMatchers(HttpMethod.GET, "/batches/{batchId}/courses").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH","ASSOCIATE");

                // Enrollment Controller
                auth.requestMatchers(HttpMethod.POST, "/enrollment").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.GET, "/enrollment/{id}").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH");
                auth.requestMatchers(HttpMethod.GET, "/enrollment").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH");
                auth.requestMatchers(HttpMethod.GET, "/enrollment/batch").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH");
                auth.requestMatchers(HttpMethod.GET, "/enrollment/associate").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH");
                auth.requestMatchers(HttpMethod.PUT, "/enrollment/{id}/status").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.GET, "/enrollment/status").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH");
                auth.requestMatchers(HttpMethod.DELETE, "/enrollment/{id}").hasRole("ADMIN");

                // Schedule Controller
                auth.requestMatchers(HttpMethod.POST, "/schedule").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.GET, "/schedule/{scheduleId}").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH");
                auth.requestMatchers(HttpMethod.GET, "/schedule").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH");
                auth.requestMatchers(HttpMethod.PUT, "/schedule/{scheduleId}/session-date").hasAnyRole("ADMIN","TRAINER");
                auth.requestMatchers(HttpMethod.GET, "/schedule/batch").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH","ASSOCIATE");

                // Trainer Controller
                auth.requestMatchers(HttpMethod.POST, "/trainer").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.GET, "/trainer/{trainerId}").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH","ASSOCIATE");
                auth.requestMatchers(HttpMethod.DELETE, "/trainer/{trainerId}").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.GET, "/trainer").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH");
                auth.requestMatchers(HttpMethod.GET, "/trainer/technology").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH");
                auth.requestMatchers(HttpMethod.PUT, "/trainer/{trainerId}/technologies").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.GET, "/trainer/{trainerId}/technologies").hasAnyRole("ADMIN","TRAINER","TECH_LEAD","COACH");

                // Assessment Controller
                auth.requestMatchers(HttpMethod.GET, "/assessments").hasAnyRole("TRAINER","ASSOCIATE");
                auth.requestMatchers(HttpMethod.GET, "/assessments/batch/{batchId}").hasAnyRole("TRAINER","ASSOCIATE");
                auth.requestMatchers(HttpMethod.GET, "/assessments/type/{type}").hasAnyRole("TRAINER","ASSOCIATE");
                auth.requestMatchers(HttpMethod.GET, "/assessments/status/{status}").hasRole("TRAINER");
                auth.requestMatchers(HttpMethod.GET, "/assessments/batch/{batchId}/type/{type}").hasAnyRole("TRAINER","ASSOCIATE");
                auth.requestMatchers(HttpMethod.GET, "/assessments/batch/{batchId}/status/{status}").hasRole("TRAINER");
                auth.requestMatchers(HttpMethod.PATCH, "/assessments/{assessmentId}").hasRole("TRAINER");
                auth.requestMatchers(HttpMethod.DELETE, "/assessments/{assessmentId}").hasRole("TRAINER");

                // Assessment - Quiz Controller
                auth.requestMatchers(HttpMethod.POST, "/assessments/quiz").hasRole("TRAINER");
                auth.requestMatchers(HttpMethod.GET, "/assessments/quiz/{quizId}").hasAnyRole("TRAINER","ASSOCIATE");
                auth.requestMatchers(HttpMethod.GET, "/assessments/quiz/batch/{batchId}").hasAnyRole("TRAINER","ASSOCIATE");
                auth.requestMatchers(HttpMethod.GET, "/assessments/quiz/batch/{batchId}/status/{status}").hasRole("TRAINER");
                auth.requestMatchers(HttpMethod.POST, "/assessments/quiz/{quizId}/attempt").hasRole("ASSOCIATE");
                auth.requestMatchers(HttpMethod.GET, "/assessments/quiz/{quizId}/attempts/{associateId}/result").hasAnyRole("TRAINER","ASSOCIATE");
                auth.requestMatchers(HttpMethod.GET, "/assessments/quiz/{quizId}/attempts").hasRole("TRAINER");

                // Assessment - Interview Controller
                auth.requestMatchers(HttpMethod.POST, "/assessments/interview").hasRole("TRAINER");
                auth.requestMatchers(HttpMethod.GET, "/assessments/interview/{interviewId}").hasAnyRole("TRAINER","ASSOCIATE");
                auth.requestMatchers(HttpMethod.GET, "/assessments/interview/batch/{batchId}").hasAnyRole("TRAINER","ASSOCIATE");
                auth.requestMatchers(HttpMethod.GET, "/assessments/interview/batch/{batchId}/category/{category}").hasAnyRole("TRAINER","ASSOCIATE");
                auth.requestMatchers(HttpMethod.POST, "/assessments/interview/{interviewId}/publish").hasRole("TRAINER");
                auth.requestMatchers(HttpMethod.GET, "/assessments/interview/{interviewId}/results").hasRole("TRAINER");
                auth.requestMatchers(HttpMethod.GET, "/assessments/interview/{interviewId}/associate/{associateId}").hasAnyRole("TRAINER","ASSOCIATE");

                // Assessment - Rubric Controller
                auth.requestMatchers(HttpMethod.POST, "/assessments/{assessmentId}/rubrics").hasRole("TRAINER");
                auth.requestMatchers(HttpMethod.GET, "/assessments/{assessmentId}/rubrics").authenticated();
                auth.requestMatchers(HttpMethod.GET, "/assessments/{assessmentId}/rubrics/total-weight").hasRole("TRAINER");
                auth.requestMatchers(HttpMethod.DELETE, "/assessments/{assessmentId}/rubrics/{rubricId}").hasRole("TRAINER");

                auth.anyRequest().permitAll();
            });
        httpSecurity.csrf(csrf->csrf.disable());
        httpSecurity.formLogin(form->form.disable());
        httpSecurity.httpBasic(httpBasic->httpBasic.disable());
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        System.out.println("Cors Config loaded");
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedHeaders(List.of("*"));  // THIS WAS MISSING - Required for preflight
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setExposedHeaders(Arrays.asList("Content-Type", "Authorization", "Set-Cookie"));
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
