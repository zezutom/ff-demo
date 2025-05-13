package com.tomaszezula.ff_demo.service

import com.tomaszezula.ff_demo.model.SubscriptionPlan
import com.tomaszezula.ff_demo.model.entity.ExperimentRepository
import com.tomaszezula.ff_demo.model.entity.UserExperiment
import com.tomaszezula.ff_demo.model.entity.UserExperimentRepository
import com.tomaszezula.ff_demo.model.isFree
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class ExperimentService(
    private val experimentRepository: ExperimentRepository,
    private val userExperimentRepository: UserExperimentRepository,
) {
    companion object {
        const val FIRST_PURCHASE = "FIRST_PURCHASE"
    }

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val rules: Map<String, PlanChangeEvaluator> = mapOf(
        FIRST_PURCHASE to { oldPlan, newPlan ->
            // If the user is changing from a free plan to a paid plan
            oldPlan.isFree() && !newPlan.isFree()
        },
    )

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    suspend fun evaluate(userId: Long, oldPlan: SubscriptionPlan, newPlan: SubscriptionPlan) {
        val experiment = experimentRepository.findByActiveTrue().firstOrNull { activeExperiment ->
            rules[activeExperiment.name]?.invoke(oldPlan, newPlan) == true
        } ?: return

        when (experiment.name) {
            FIRST_PURCHASE -> {
                logger.debug("User {} is eligible for the {} experiment", userId, FIRST_PURCHASE)
                userExperimentRepository.save(
                    UserExperiment(
                        userId = userId,
                        experimentId = experiment.id!!,
                    )
                )
            }
            else -> {
                // Handle other experiments if needed
            }
        }
    }
}

typealias PlanChangeEvaluator = (SubscriptionPlan,  SubscriptionPlan) -> Boolean
