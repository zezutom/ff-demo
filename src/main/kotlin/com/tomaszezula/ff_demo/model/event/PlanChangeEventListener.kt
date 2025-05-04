package com.tomaszezula.ff_demo.model.event

import com.tomaszezula.ff_demo.service.ExperimentService
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PlanChangeEventListener(
    private val experimentService: ExperimentService,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener
    fun handlePlanChange(event: PlanChangeEvent) {
        runBlocking {
            logger.debug("Handling PlanChangeEvent for userId: {}, oldPlan: {}, newPlan: {}",
                event.userId,
                event.oldPlan,
                event.newPlan
            )
            experimentService.evaluate(
                userId = event.userId,
                oldPlan = event.oldPlan,
                newPlan = event.newPlan
            )
        }
    }
}
