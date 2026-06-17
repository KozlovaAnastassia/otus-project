package ru.otus.otuskotlin.app.kafka

class AppKafkaConfig(
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaTopicInV1: String = KAFKA_TOPIC_IN_V1,
    val kafkaTopicOutV1: String = KAFKA_TOPIC_OUT_V1,
) {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_V1_VAR = "KAFKA_TOPIC_IN_V1"
        const val KAFKA_TOPIC_OUT_V1_VAR = "KAFKA_TOPIC_OUT_V1"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "localhost:9092").split("\\s*[,; ]\\s*") }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "meme-service" }
        val KAFKA_TOPIC_IN_V1 by lazy { System.getenv(KAFKA_TOPIC_IN_V1_VAR) ?: "meme-v1-in" }
        val KAFKA_TOPIC_OUT_V1 by lazy { System.getenv(KAFKA_TOPIC_OUT_V1_VAR) ?: "meme-v1-out" }
    }
}

suspend fun AppKafkaConfig.controllerHelper(
    blockDeserialize: suspend () -> Unit,
    blockSerialize: suspend () -> String,
    logId: String
): String {
    blockDeserialize()
    return blockSerialize()
}