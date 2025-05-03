package com.tomaszezula.ff_demo.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

@Table(name = "users")
data class User(
    @Id
    val id: Long,
    @Column("subscription_plan")
    var subscriptionPlan: String
)

interface UserRepository : CoroutineCrudRepository<User, Long>
