package ru.otus.otuskotlin.app.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.api.v1.apiV1RequestSerialize
import ru.otus.otuskotlin.api.v1.apiV1ResponseDeserialize
import ru.otus.otuskotlin.api.v1.models.*

class KafkaControllerTest {

    @Test
    fun `should process create meme request via kafka`() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(
            config,
            ConsumerStrategyV1(),
            consumer = consumer,
            producer = producer
        )

        val request = MemeCreateRequest(
            requestType = "create",
            debug = MemeDebug(
                mode = MemeRequestDebugMode.stub,
                stub = MemeRequestDebugStubs.success
            ),
            meme = MemeCreateObject(
                title = "Тестовый мем",
                tags = listOf("кот", "смешное"),
                image = "base64image"
            )
        )

        consumer.schedulePollTask {
            consumer.rebalance(listOf(TopicPartition(inputTopic, PARTITION)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-key",
                    apiV1RequestSerialize(request)
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        startOffsets[TopicPartition(inputTopic, PARTITION)] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val sentMessage = producer.history().first()
        val response = apiV1ResponseDeserialize<MemeCreateResponse>(sentMessage.value())

        assertEquals(outputTopic, sentMessage.topic())
        assertEquals("Тестовый мем", response.meme?.title)
    }

    @Test
    fun `should process read meme request via kafka`() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, ConsumerStrategyV1(), consumer = consumer, producer = producer)

        val request = MemeReadRequest(
            requestType = "read",
            debug = MemeDebug(
                mode = MemeRequestDebugMode.stub,
                stub = MemeRequestDebugStubs.success
            ),
            meme = MemeReadObject(id = "123")
        )

        consumer.schedulePollTask {
            consumer.rebalance(listOf(TopicPartition(inputTopic, PARTITION)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-key",
                    apiV1RequestSerialize(request)
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        startOffsets[TopicPartition(inputTopic, PARTITION)] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val sentMessage = producer.history().first()
        val response = apiV1ResponseDeserialize<MemeReadResponse>(sentMessage.value())

        assertEquals(outputTopic, sentMessage.topic())
        assertEquals("123", response.meme?.id)
    }

    @Test
    fun `should process search meme request via kafka`() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, ConsumerStrategyV1(), consumer = consumer, producer = producer)

        val request = MemeSearchRequest(
            requestType = "search",
            debug = MemeDebug(
                mode = MemeRequestDebugMode.stub,
                stub = MemeRequestDebugStubs.success
            ),
            memeFilter = MemeSearchFilter(
                searchString = "кот",
                tags = listOf("смешное")
            )
        )

        consumer.schedulePollTask {
            consumer.rebalance(listOf(TopicPartition(inputTopic, PARTITION)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-key",
                    apiV1RequestSerialize(request)
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        startOffsets[TopicPartition(inputTopic, PARTITION)] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val sentMessage = producer.history().first()
        val response = apiV1ResponseDeserialize<MemeSearchResponse>(sentMessage.value())

        assertEquals(outputTopic, sentMessage.topic())
        assertEquals(2, response.memes?.size)
    }

    companion object {
        const val PARTITION = 0
    }
}


