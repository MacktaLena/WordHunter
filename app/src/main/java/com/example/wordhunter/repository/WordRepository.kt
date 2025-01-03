package com.example.wordhunter.repository

import com.example.wordhunter.api.ApiWord
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class WordRepository(private val httpClient: HttpClient) {

    suspend fun loadWordPlant(): String {
        val result: List<ApiWord> = httpClient.get("https://www.wordgamedb.com/api/v1/words/?category=plant").body()
        return if (result.isNotEmpty()) {
            val randomWord = result.random()
            randomWord.word
        } else {
            "No words found"
        }
    }

    suspend fun loadWordCountry(): String {
        val result: List<ApiWord> = httpClient.get("https://www.wordgamedb.com/api/v1/words/?category=country").body()
        return if (result.isNotEmpty()) {
            val randomWord = result.random()
            randomWord.word
        } else {
            "No words found"
        }
    }
}