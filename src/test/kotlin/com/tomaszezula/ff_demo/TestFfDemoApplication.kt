package com.tomaszezula.ff_demo

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<FfDemoApplication>().with(TestcontainersConfiguration::class).run(*args)
}
