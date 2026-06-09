package ru.otus.otuskotlin.api.v1

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import ru.otus.otuskotlin.api.v1.models.IRequest
import ru.otus.otuskotlin.api.v1.models.IResponse

val apiV1Mapper = JsonMapper.builder().run {
    addModule(KotlinModule.Builder().build())
    addModule(JavaTimeModule())
    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
    build()
}

fun apiV1RequestSerialize(request: IRequest): String = apiV1Mapper.writeValueAsString(request)

inline fun <reified T : IRequest> apiV1RequestDeserialize(json: String): T =
    apiV1Mapper.readValue(json, T::class.java)

fun apiV1ResponseSerialize(response: IResponse): String = apiV1Mapper.writeValueAsString(response)

inline fun <reified T : IResponse> apiV1ResponseDeserialize(json: String): T =
    apiV1Mapper.readValue(json, T::class.java)