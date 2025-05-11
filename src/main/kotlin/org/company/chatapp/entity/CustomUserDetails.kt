package org.company.chatapp.entity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    val id: Long,
    private val usernameValue: String,
    private val passwordValue: String
) : UserDetails {

    override fun getUsername(): String = usernameValue
    override fun getPassword(): String = passwordValue

    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true

    companion object {
        fun fromUserEntity(user: UserEntity): CustomUserDetails {
            return CustomUserDetails(
                id = user.id,
                usernameValue = user.username,
                passwordValue = user.password
            )
        }
    }
}