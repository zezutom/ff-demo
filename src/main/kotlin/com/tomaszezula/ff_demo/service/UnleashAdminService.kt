package com.tomaszezula.ff_demo.service

import com.tomaszezula.ff_demo.model.AdminFeaturesResponse
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientException

@Service
class UnleashAdminService(
    private val client: WebClient,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun featureNames(): List<String> {
        return try {
            val response: AdminFeaturesResponse = client
                .get()
                .uri("/features")
                .retrieve()
                .bodyToMono(AdminFeaturesResponse::class.java)
                .awaitSingle() ?: AdminFeaturesResponse(emptyList())
            response.features.map { it.name }
        } catch (e: WebClientException) {
            logger.error("Error fetching feature names from Unleash admin API: ${e.message}", e)
            emptyList()
        }

    }
}
