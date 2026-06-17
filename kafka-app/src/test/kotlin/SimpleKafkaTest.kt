package ru.otus.otuskotlin.app.kafka

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant
import java.util.*

@Disabled("требуется docker-compose up -d")
class SimpleKafkaTest {
    private val topicName = "meme-v1-in"

    @Test
    fun producerTest() {
        val props = Properties().apply {
            put("bootstrap.servers", "localhost:9092")
            put("acks", "all")
            put("retries", 0)
            put("batch.size", 16384)
            put("buffer.memory", 33554432)
            put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
            put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        }

        KafkaProducer<String, String>(props).use { producer ->
            (0..<10).forEach { it ->
                val key = "key#$it"
                val value = "Message number $it"
                producer.send(ProducerRecord(topicName, key, value))
            }
            println("Message sent successfully")
        }
    }

    @Test
    fun consumerTest() {
        val props = Properties().apply {
            put("bootstrap.servers", "localhost:9092")
            put("group.id", "meme-test-group")
            put("enable.auto.commit", "true")
            put("auto.commit.interval.ms", "1000")
            put("session.timeout.ms", "30000")
            put("auto.offset.reset", "earliest")
            put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
            put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        }

        KafkaConsumer<String, String>(props).use { consumer ->
            consumer.subscribe(listOf(topicName))
            val timeout = Instant.now() + Duration.ofSeconds(10)

            while (Instant.now() < timeout) {
                val records = consumer.poll(Duration.ofMillis(1000))
                records.forEach { record ->
                    println("topic = ${record.topic()}, offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}")
                }
            }
        }
    }
}