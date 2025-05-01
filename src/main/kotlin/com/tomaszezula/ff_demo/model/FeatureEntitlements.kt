package com.tomaszezula.ff_demo.model

data class FeatureEntitlements(
    val userId: Long,
    val subscriptionPlan: SubscriptionPlan,
    val featureFlags: List<FeatureFlag>
)
