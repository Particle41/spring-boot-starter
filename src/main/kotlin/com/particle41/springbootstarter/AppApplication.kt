package com.particle41.springbootstarter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AppApplication

fun main(args: Array<String>) {
	runApplication<AppApplication>(*args)
}
