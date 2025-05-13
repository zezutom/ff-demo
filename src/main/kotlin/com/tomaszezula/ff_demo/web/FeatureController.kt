package com.tomaszezula.ff_demo.web

import com.tomaszezula.ff_demo.model.UserFeatures
import com.tomaszezula.ff_demo.model.SubscriptionPlan
import com.tomaszezula.ff_demo.service.FeatureEntitlementService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class FeatureController(
    private val featureEntitlementService: FeatureEntitlementService,
) {

    @QueryMapping
    suspend fun user(@Argument id: Long): UserFeatures {
        return featureEntitlementService.getUserFeatures(id)
    }

    @MutationMapping
    suspend fun updateUserPlan(
        @Argument id: Long,
        @Argument subscriptionPlan: SubscriptionPlan
    ): UserFeatures {
        // Let the magic happen
        featureEntitlementService.updateUserPlan(id, subscriptionPlan)
        // Fetch the updated user features
        return featureEntitlementService.getUserFeatures(id)
    }

}
