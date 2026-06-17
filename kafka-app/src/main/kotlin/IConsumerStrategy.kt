package ru.otus.otuskotlin.app.kafka

import ru.otus.otuskotlin.common.MemeContext

interface IConsumerStrategy {
    fun topics(config: AppKafkaConfig): InputOutputTopics
    fun serialize(source: MemeContext): String
    fun deserialize(value: String, target: MemeContext)
}