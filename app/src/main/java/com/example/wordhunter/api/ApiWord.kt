package com.example.wordhunter.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiWord(val word: String, val hint: String)