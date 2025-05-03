package com.tomaszezula.ff_demo.config

import io.getunleash.DefaultUnleash
import io.getunleash.Unleash
import io.getunleash.util.UnleashConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class UnleashConfig(
    @Value("\${unleash.api-url}") private val apiUrl: String,
    @Value("\${unleash.api-key}") private val apiKey: String,
    @Value("\${unleash.admin-api-url}") private val adminApiUrl: String,
    @Value("\${unleash.admin-api-key}") private val adminApiKey: String,
) {

    @Bean
    fun unleash(): Unleash {
        val config = UnleashConfig.builder()
            .appName("ff-demo")
            .instanceId("ff-demo-instance")
            .unleashAPI(apiUrl)
            .apiKey(apiKey)
//            .fallbackStrategy(SubscriptionStrategy())
            .build()
        return DefaultUnleash(config)
    }

    @Bean
    fun unleashAdminClient(): WebClient = WebClient.builder()
        .baseUrl(adminApiUrl)
        .defaultHeader("Authorization", adminApiKey)
        .build()
}
