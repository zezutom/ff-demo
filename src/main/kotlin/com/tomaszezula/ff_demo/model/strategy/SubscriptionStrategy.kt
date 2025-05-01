package com.tomaszezula.ff_demo.model.strategy

import io.getunleash.UnleashContext
import io.getunleash.strategy.Strategy

class SubscriptionStrategy : Strategy {

    override fun getName(): String = this.javaClass.simpleName

    override fun isEnabled(params: MutableMap<String, String>, context: UnleashContext): Boolean {
        val plan = context.properties["plan"] ?: return false
        return params["allowedPlans"]
            ?.split(",")
            ?.map { it.trim().uppercase() }
            ?.contains(plan.uppercase())
            ?: false
    }
}
