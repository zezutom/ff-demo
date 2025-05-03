package com.tomaszezula.ff_demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
class FfDemoApplication

fun main(args: Array<String>) {
	runApplication<FfDemoApplication>(*args)
}
