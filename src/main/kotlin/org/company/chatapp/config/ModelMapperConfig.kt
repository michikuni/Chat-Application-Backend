package org.company.chatapp.config

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ModelMapperConfig {
    @Bean
    fun modelMapper(): ModelMapper {
        val mapper = ModelMapper()
        mapper.configuration.setMatchingStrategy(MatchingStrategies.STRICT)
        return mapper
    }
}