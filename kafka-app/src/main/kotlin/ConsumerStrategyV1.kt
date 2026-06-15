package ru.otus.otuskotlin.app.kafka

import ru.otus.otuskotlin.api.v1.apiV1RequestDeserialize
import ru.otus.otuskotlin.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.api.v1.models.IRequest
import ru.otus.otuskotlin.api.v1.models.IResponse
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.mappers.v1.fromTransport
import ru.otus.otuskotlin.mappers.v1.toTransportMeme

class ConsumerStrategyV1 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: MemeContext): String {
        val response: IResponse = source.toTransportMeme()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: MemeContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}