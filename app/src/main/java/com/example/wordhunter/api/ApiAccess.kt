package com.example.wordhunter.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiAccess {
    fun createHttpClient(): HttpClient {
        val client = HttpClient(Android) {
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
        return client
    }
}