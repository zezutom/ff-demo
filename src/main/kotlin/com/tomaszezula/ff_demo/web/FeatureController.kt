package com.tomaszezula.ff_demo.web

import com.tomaszezula.ff_demo.model.FeatureEntitlements
import com.tomaszezula.ff_demo.service.FeatureEntitlementService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class FeatureController(
    private val featureEntitlementService: FeatureEntitlementService,
) {

    @QueryMapping
    fun featureEntitlements(@Argument userId: Long): FeatureEntitlements {
        return featureEntitlementService.entitlements(userId)
    }
}
