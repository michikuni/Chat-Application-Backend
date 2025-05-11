    package org.company.chatapp.entity

    import com.fasterxml.jackson.annotation.JsonInclude
    import jakarta.persistence.*
    import java.time.LocalDateTime

    @Entity
    @Table(name = "users")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class UserEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(nullable = false)
        val name: String,

        @Column(nullable = false, unique = true)
        val username: String,

        @Column(nullable = false, unique = true)
        val email: String,

        @Column(nullable = false)
        var password: String,

        @Column(nullable = true)
        val avatar: String? = null,

        val createAt: LocalDateTime = LocalDateTime.now(),
    )