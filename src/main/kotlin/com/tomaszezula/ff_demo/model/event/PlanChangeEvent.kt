package com.tomaszezula.ff_demo.model.event

import com.tomaszezula.ff_demo.model.SubscriptionPlan
import org.springframework.context.ApplicationEvent

data class PlanChangeEvent(
    val userId: Long,
    val oldPlan: SubscriptionPlan,
    val newPlan: SubscriptionPlan,
) : ApplicationEvent(userId)
