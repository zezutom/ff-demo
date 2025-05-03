package com.tomaszezula.ff_demo.config

import com.tomaszezula.ff_demo.model.SubscriptionPlan
import com.tomaszezula.ff_demo.model.User
import com.tomaszezula.ff_demo.model.UserRepository
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
class DataConfig(
    private val userRepository: UserRepository
) {

    @Bean
    fun transactionManager(cf: ConnectionFactory): ReactiveTransactionManager {
        return R2dbcTransactionManager(cf)
    }

    @Bean
    fun loadInitialUsers() = ApplicationRunner {
        runBlocking {
            listOf(
                User(1, SubscriptionPlan.BASIC.name),
                User(2, SubscriptionPlan.BASIC.name),
            ).forEach { user ->
                if (!userRepository.existsById(user.id)) {
                    userRepository.save(user)
                }
            }
        }
    }
}
