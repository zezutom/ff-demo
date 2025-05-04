package com.tomaszezula.ff_demo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

@Table(name = "experiments")
data class Experiment(
    @Id
    override val id: Long? = null,
    val name: String,
    val description: String?,
    val active: Boolean = true,
) : Entity

interface ExperimentRepository : CoroutineCrudRepository<Experiment, Long> {
    suspend fun findByActiveTrue(): List<Experiment>
}
