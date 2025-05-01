package com.tomaszezula.ff_demo.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "users")
data class User(
    @Id
    val id: Long,
    @Column(name = "subscription_plan")
    val subscriptionPlan: String
)

interface UserRepository : JpaRepository<User, Long>
