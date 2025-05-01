package com.tomaszezula.ff_demo.service

import com.tomaszezula.ff_demo.model.FeatureEntitlements
import com.tomaszezula.ff_demo.model.SubscriptionPlan
import com.tomaszezula.ff_demo.model.UserRepository
import org.springframework.stereotype.Service

@Service
class FeatureEntitlementService(
    private val userRepository: UserRepository,
    private val featureService: FeatureService,
) {

    fun entitlements(userId: Long): FeatureEntitlements {
        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("User with ID $userId not found")
        }
        val plan = SubscriptionPlan.valueOf(user.subscriptionPlan.uppercase())
        val flags = featureService.featureFlags(userId, plan)
        return FeatureEntitlements(
            userId = userId,
            subscriptionPlan = plan,
            featureFlags = flags
        )
    }
}
