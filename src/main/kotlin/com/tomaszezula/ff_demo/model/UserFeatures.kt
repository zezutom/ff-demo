package com.tomaszezula.ff_demo.model

data class UserFeatures(
    val id: Long,
    val subscriptionPlan: SubscriptionPlan,
    val featureFlags: List<FeatureFlag>
)
