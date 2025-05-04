package com.tomaszezula.ff_demo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

@Table(name = "users")
data class User(
    @Id
    override val id: Long,
    @Column("subscription_plan")
    var subscriptionPlan: String
) : Entity

interface UserRepository : CoroutineCrudRepository<User, Long>
