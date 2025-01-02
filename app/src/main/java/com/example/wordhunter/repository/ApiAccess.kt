package com.example.wordhunter.repository

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ApiWord(val word: String, val hint: String)

fun createHttpClient() = HttpClient(Android) {
    expectSuccess = true
    engine {
        connectTimeout = 60000
    }
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}