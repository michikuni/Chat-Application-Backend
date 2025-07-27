package org.company.chatapp.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.company.chatapp.service.CustomUserDetailsService
import org.company.chatapp.utils.JwtUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter (
    private val jwtUtils: JwtUtils,
    private val customUserDetails: CustomUserDetailsService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        println("Authorization Header: $authHeader")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authHeader.substring(7)
        println("ok8$jwt")
        val username = jwtUtils.extractUsername(jwt)

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = customUserDetails.loadUserByUsername(username)
            if (jwtUtils.validateToken(jwt, userDetails.username)) {
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities

                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        println("ok9")
        filterChain.doFilter(request, response)
    }
}