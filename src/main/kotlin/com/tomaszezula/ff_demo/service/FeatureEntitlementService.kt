package com.tomaszezula.ff_demo.service

import com.tomaszezula.ff_demo.model.SubscriptionPlan
import com.tomaszezula.ff_demo.model.UserFeatures
import com.tomaszezula.ff_demo.model.entity.UserRepository
import com.tomaszezula.ff_demo.model.event.PlanChangeEvent
import com.tomaszezula.ff_demo.model.event.registerAfterCommit
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeatureEntitlementService(
    private val userRepository: UserRepository,
    private val featureService: FeatureService,
    private val publisher: ApplicationEventPublisher,
) {

    suspend fun getUserFeatures(userId: Long): UserFeatures {
        val user = userRepository.findById(userId) ?: run {
            throw IllegalArgumentException("User with ID $userId not found")
        }
        val plan = SubscriptionPlan.valueOf(user.subscriptionPlan.uppercase())
        val flags = featureService.featureFlagsInternal(userId, plan)
        return UserFeatures(
            id = userId,
            subscriptionPlan = plan,
            featureFlags = flags
        )
    }

    @Transactional
    suspend fun updateUserPlan(userId: Long, subscriptionPlan: SubscriptionPlan) {
        val user = userRepository.findById(userId) ?: run {
            throw IllegalArgumentException("User with ID $userId not found")
        }
        val oldPlan = SubscriptionPlan.valueOf(user.subscriptionPlan)
        user.subscriptionPlan = subscriptionPlan.name
        userRepository.save(user)

        // Publish the event after the user plan has been updated
        registerAfterCommit {
            publisher.publishEvent(
                PlanChangeEvent(
                    userId = userId,
                    oldPlan = oldPlan,
                    newPlan = subscriptionPlan
                )
            )
        }
    }
}
