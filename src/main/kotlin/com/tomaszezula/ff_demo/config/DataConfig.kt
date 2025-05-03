package com.tomaszezula.ff_demo.config

import com.tomaszezula.ff_demo.model.SubscriptionPlan
import com.tomaszezula.ff_demo.model.User
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
class DataConfig(
    private val template: R2dbcEntityTemplate,
) {

    @Bean
    fun transactionManager(cf: ConnectionFactory): ReactiveTransactionManager {
        return R2dbcTransactionManager(cf)
    }

    @Bean
    fun loadInitialUsers() = ApplicationRunner {
        runBlocking {
            listOf(
                User(1, SubscriptionPlan.FREE_TRIAL.name),
                User(2, SubscriptionPlan.FREE_TRIAL.name),
            ).forEach { user ->
                val exists = template.exists(
                    Query.query(Criteria.where("id").`is`(user.id)),
                    User::class.java
                ).awaitSingle()
                if (!exists) {
                    template.insert(User::class.java)
                        .using(user)
                        .awaitSingle()
                }
            }
        }
    }
}
