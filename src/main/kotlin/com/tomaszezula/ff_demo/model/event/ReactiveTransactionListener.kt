package com.tomaszezula.ff_demo.model.event

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.transaction.reactive.TransactionSynchronization
import org.springframework.transaction.reactive.TransactionSynchronizationManager
import reactor.core.publisher.Mono

suspend fun registerAfterCommit(onCommit: () -> Unit) {
    val syncManager = TransactionSynchronizationManager.forCurrentTransaction().awaitSingle()
    syncManager.registerSynchronization(object : TransactionSynchronization {
        override fun afterCommit(): Mono<Void> {
            return Mono.fromRunnable<Unit> {
                onCommit()
            }.then()
        }
    })
}
