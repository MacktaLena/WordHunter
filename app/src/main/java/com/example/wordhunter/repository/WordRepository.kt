package com.example.wordhunter.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wordhunter.api.ApiWord
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.delay

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
        delay(3000)
        val result: List<ApiWord> = httpClient.get("https://www.wordgamedb.com/api/v1/words/?category=country").body()
        return if (result.isNotEmpty()) {
            val randomWord = result.random()
            randomWord.word
        } else {
            "No words found"
        }
    }


    // hier die readAll-Funktionen, wie ich sie für den Block LiveData umgesetzt habe
    fun readAll(level: String): LiveData<String> {
        val wordToGuess = MutableLiveData("")
        val easyWords = listOf("nest", "mauer", "schnee", "herr", "stern", "reise")
        val difficultWords =
            listOf("zucchini", "krautkopf", "feuerwehr", "terrasse", "brillant", "recycling")

        if (level == "easy") {
            wordToGuess.setValue(easyWords.random().uppercase())
        } else {
            wordToGuess.setValue(difficultWords.random().uppercase())
        }
        return wordToGuess
    }
}