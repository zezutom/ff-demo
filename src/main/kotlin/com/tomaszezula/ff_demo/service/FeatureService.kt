package com.tomaszezula.ff_demo.service

import com.tomaszezula.ff_demo.model.FeatureFlag
import com.tomaszezula.ff_demo.model.SubscriptionPlan
import com.tomaszezula.ff_demo.model.entity.ExperimentRepository
import com.tomaszezula.ff_demo.model.entity.UserExperimentRepository
import io.getunleash.Unleash
import io.getunleash.UnleashContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class FeatureService(
    private val unleash: Unleash,
    private val adminService: UnleashAdminService,
    private val experimentRepository: ExperimentRepository,
    private val userExperimentRepository: UserExperimentRepository,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Cacheable("flags", key = "#plan")
    suspend fun featureFlags(userId: Long, plan: SubscriptionPlan): List<FeatureFlag> {
        return featureFlagsInternal(userId, plan)
    }

    suspend fun featureFlagsInternal(
        userId: Long,
        plan: SubscriptionPlan,
    ) = try {
        val contextBuilder = UnleashContext.builder()
            .userId(userId.toString())
            .addProperty("plan", plan.name)

        // TODO This is currently limited to a single experiment per user
        userExperimentRepository.findByUserId(userId).firstOrNull()?.let {
            experimentRepository.findById(it.experimentId)
        }?.let { experiment ->
            contextBuilder.addProperty("experiment", experiment.name)
        }

        val featureNames = adminService.featureNames()
        featureNames.map { featureName ->
            FeatureFlag(
                key = featureName,
                enabled = withContext(Dispatchers.IO) {
                    unleash.isEnabled(featureName, contextBuilder.build())
                }
            )
        }
    } catch (e: Exception) {
        logger.error("Error fetching feature flags for user $userId with plan $plan: ${e.message}", e)
        emptyList()
    }
}
