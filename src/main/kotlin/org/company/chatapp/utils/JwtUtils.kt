package org.company.chatapp.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtils {
    private val secret: SecretKey = Keys.hmacShaKeyFor("a1f4b9c2d75e3f8a6e1b9cd0f3471e89a023c17de8fa2bd6c57c1a3e90d4f2b1".toByteArray())
    private val expirationTime: Long = 86400000

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(secret)
            .compact()
    }

    fun extractUsername(token: String): String? {
        return Jwts.parserBuilder()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun validateToken(authToken: String, username: String): Boolean {
        val tokenUsername = extractUsername(authToken)
        return tokenUsername == username && !isTokenExpired(authToken)
    }

    private fun isTokenExpired(authToken: String): Boolean {
        return Jwts.parserBuilder()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(authToken)
            .body
            .expiration
            .before(Date())
    }
}