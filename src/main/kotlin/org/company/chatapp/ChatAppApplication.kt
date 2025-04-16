package org.company.chatapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableWebSecurity
@SpringBootApplication
class ChatAppApplication

fun main(args: Array<String>) {
    runApplication<ChatAppApplication>(*args)
}
