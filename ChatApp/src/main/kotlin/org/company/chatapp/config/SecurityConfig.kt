package org.company.chatapp.config

import org.company.chatapp.filter.JwtAuthFilter
import org.company.chatapp.repository.UserRepository
import org.company.chatapp.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthFilter: JwtAuthFilter
    ): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/auth/**").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService {
        return CustomUserDetailsService(userRepository)
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder{
        return BCryptPasswordEncoder()
    }
}