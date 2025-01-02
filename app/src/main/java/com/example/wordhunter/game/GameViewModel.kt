package com.example.wordhunter.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class GameViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    fun readLevel(): String {
        return savedStateHandle["EXTRA_KEY_LEVEL"] ?: "plant"
    }
}
