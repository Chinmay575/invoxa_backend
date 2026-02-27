package com.chinmaysinghmodak.invoicing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class InvoicingApplication


fun main(args: Array<String>) {
    runApplication<InvoicingApplication>(*args)
}
