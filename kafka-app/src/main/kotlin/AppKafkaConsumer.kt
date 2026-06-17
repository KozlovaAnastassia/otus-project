package ru.otus.otuskotlin.app.kafka

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.WakeupException
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.common.MemeContext
import ru.otus.otuskotlin.common.models.MemeState
import ru.otus.otuskotlin.common.models.MemeWorkMode
import ru.otus.otuskotlin.common.stubs.MemeStubs
import java.time.Duration
import java.util.*

class AppKafkaConsumer(
    private val config: AppKafkaConfig,
    private val strategy: IConsumerStrategy,
    private val consumer: Consumer<String, String> = createKafkaConsumer(config),
    private val producer: Producer<String, String> = createKafkaProducer(config)
) : AutoCloseable {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val process = atomic(true)

    fun start(): Unit = runBlocking { startSusp() }

    suspend fun startSusp() {
        process.value = true
        try {
            val topics = strategy.topics(config)
            consumer.subscribe(listOf(topics.input))

            while (process.value) {
                val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
                    consumer.poll(Duration.ofSeconds(1))
                }

                records.forEach { record ->
                    try {
                        val context = MemeContext().apply {
                            workMode = MemeWorkMode.STUB
                            stubCase = MemeStubs.SUCCESS
                        }
                        strategy.deserialize(record.value(), context)

                        processRequest(context)

                        val response = strategy.serialize(context)
                        sendResponse(response, topics.output)
                    } catch (ex: Exception) {
                        log.error("Error message", ex)
                    }
                }
            }
        } catch (ex: WakeupException) {
            // TODO
        } finally {
            withContext(NonCancellable) {
                consumer.close()
                producer.close()
            }
        }
    }

    private fun processRequest(context: MemeContext) {
        when (context.command) {
            ru.otus.otuskotlin.common.models.MemeCommand.CREATE -> {
                context.memeResponse = ru.otus.otuskotlin.common.models.Meme(
                    id = ru.otus.otuskotlin.common.models.MemeId("123"),
                    title = context.memeRequest.title,
                    tags = context.memeRequest.tags,
                    image = context.memeRequest.image,
                    createdAt = kotlinx.datetime.Clock.System.now()
                )
            }
            ru.otus.otuskotlin.common.models.MemeCommand.READ -> {
                val requestId = context.memeRequest?.id ?: ru.otus.otuskotlin.common.models.MemeId.NONE
                context.memeResponse = ru.otus.otuskotlin.common.models.Meme(
                    id = requestId,
                    title = "Мем ${requestId}",
                    tags = listOf("тест"),
                    image = "/uploads/test.jpg",
                    createdAt = kotlinx.datetime.Clock.System.now()
                )
            }
            ru.otus.otuskotlin.common.models.MemeCommand.SEARCH -> {
                context.memesResponse = mutableListOf(
                    ru.otus.otuskotlin.common.models.Meme(
                        id = ru.otus.otuskotlin.common.models.MemeId("1"),
                        title = "Первый мем",
                        tags = listOf("кот"),
                        image = "/uploads/1.jpg"
                    ),
                    ru.otus.otuskotlin.common.models.Meme(
                        id = ru.otus.otuskotlin.common.models.MemeId("2"),
                        title = "Второй мем",
                        tags = listOf("собака"),
                        image = "/uploads/2.jpg"
                    )
                )
            }
            else -> {}
        }
        context.state = MemeState.FINISHING
    }

    private suspend fun sendResponse(json: String, outputTopic: String) {
        val resRecord = ProducerRecord(outputTopic, UUID.randomUUID().toString(), json)
        log.info("Sending to $outputTopic: $json")
        withContext(Dispatchers.IO) {
            producer.send(resRecord)
        }
    }

    override fun close() {
        process.value = false
    }

    companion object {
        fun createKafkaConsumer(config: AppKafkaConfig): KafkaConsumer<String, String> {
            val props = Properties().apply {
                put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaHosts.joinToString(","))
                put(ConsumerConfig.GROUP_ID_CONFIG, config.kafkaGroupId)
                put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
                put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
                put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
            }
            return KafkaConsumer(props)
        }

        fun createKafkaProducer(config: AppKafkaConfig): KafkaProducer<String, String> {
            val props = Properties().apply {
                put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaHosts.joinToString(","))
                put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
                put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
            }
            return KafkaProducer(props)
        }
    }
}