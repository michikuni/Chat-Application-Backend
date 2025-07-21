package org.company.chatapp.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtils {

    companion object {
        private const val SECRET_KEY = "a1f4b9c2d75e3f8a6e1b9cd0f3471e89a023c17de8fa2bd6c57c1a3e90d4f2b1"
        private const val EXPIRATION_TIME_MS: Long = 24 * 60 * 60 * 1000 // 1 day
    }

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(SECRET_KEY.toByteArray())

    fun generateToken(username: String): String {
        val claims = Jwts.claims().setSubject(username)
        return createToken(claims)
    }

    private fun createToken(claims: Map<String, Any>): String {
        val now = Date()
        val expiry = Date(now.time + EXPIRATION_TIME_MS)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun extractUsername(token: String): String? {
        return extractClaim(token) { it.subject }
    }

    fun <T> extractClaim(token: String, resolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return resolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun validateToken(token: String, user: String): Boolean {
        val username = extractUsername(token)
        return username == user && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = extractClaim(token) { it.expiration }
        return expiration.before(Date())
    }
}
