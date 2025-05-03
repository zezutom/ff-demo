package com.tomaszezula.ff_demo.service

import com.tomaszezula.ff_demo.model.UserFeatures
import com.tomaszezula.ff_demo.model.SubscriptionPlan
import com.tomaszezula.ff_demo.model.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeatureEntitlementService(
    private val userRepository: UserRepository,
    private val featureService: FeatureService,
) {

    suspend fun getUserFeatures(userId: Long): UserFeatures {
        return withContext(Dispatchers.IO) {
            getUserFeaturesInternal(userId)
        }
    }

    private suspend fun getUserFeaturesInternal(userId: Long): UserFeatures {
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
    suspend fun updateUserPlan(userId: Long, subscriptionPlan: SubscriptionPlan): UserFeatures {
        val user = userRepository.findById(userId) ?: run {
            throw IllegalArgumentException("User with ID $userId not found")
        }
        user.subscriptionPlan = subscriptionPlan.name
        userRepository.save(user)
        return getUserFeaturesInternal(userId)
    }
}
