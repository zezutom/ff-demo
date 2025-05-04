package com.tomaszezula.ff_demo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.Instant

@Table("user_experiments")
data class UserExperiment(
    @Id
    override val id: Long,
    val userId: Long,
    val segmentId: Long,
    val assignedAt: Instant = Instant.now()
) : Entity

interface UserExperimentRepository : CoroutineCrudRepository<UserExperiment, Long> {
    suspend fun findByUserId(userId: Long): List<UserExperiment>
}
