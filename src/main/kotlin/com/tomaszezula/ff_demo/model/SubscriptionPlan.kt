package com.tomaszezula.ff_demo.model

enum class SubscriptionPlan {
    FREE, FREE_TRIAL, BASIC, PRO, ENTERPRISE
}

fun SubscriptionPlan.isFree(): Boolean {
    return this == SubscriptionPlan.FREE || this == SubscriptionPlan.FREE_TRIAL
}
