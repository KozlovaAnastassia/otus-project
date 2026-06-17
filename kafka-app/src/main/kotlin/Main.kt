package ru.otus.otuskotlin.app.kafka

fun main() {
    val config = AppKafkaConfig()
    val consumer = AppKafkaConsumer(config, ConsumerStrategyV1())
    consumer.start()
}