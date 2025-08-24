package org.company.chatapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import java.util.*

@EnableWebSecurity
@SpringBootApplication
class ChatAppApplication

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("GMT+07:00"))
    runApplication<ChatAppApplication>(*args)
}
