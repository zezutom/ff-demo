package com.tomaszezula.ff_demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
@EnableAsync
class FfDemoApplication

fun main(args: Array<String>) {
	runApplication<FfDemoApplication>(*args)
}
