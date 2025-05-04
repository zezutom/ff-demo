package com.tomaszezula.ff_demo.config

import com.tomaszezula.ff_demo.model.SubscriptionPlan
import com.tomaszezula.ff_demo.model.entity.Entity
import com.tomaszezula.ff_demo.model.entity.Experiment
import com.tomaszezula.ff_demo.model.entity.User
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
            // Insert initial users
            insert(
                User(1, SubscriptionPlan.FREE_TRIAL.name),
                User(2, SubscriptionPlan.FREE_TRIAL.name),
            )
            // Insert initial experiments
            insert(
                Experiment(1, "First Purchase", "Extra features reward on the first purchase")
            )
        }
    }

    private suspend inline fun <reified E : Entity> insert(vararg entity: E) {
        entity.forEach {
            val exists = template.exists(
                Query.query(Criteria.where("id").`is`(it.id)),
                User::class.java
            ).awaitSingle()
            if (!exists) {
                template.insert(E::class.java)
                    .using(it)
                    .awaitSingle()
            }
        }
    }
}
