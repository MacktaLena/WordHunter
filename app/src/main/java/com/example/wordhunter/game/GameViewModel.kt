package com.example.wordhunter.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.wordhunter.api.ApiAccess
import com.example.wordhunter.repository.WordRepository


class GameViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    //private val apiAccess = ApiAccess()
    //private val httpClient = apiAccess.createHttpClient() // HttpClient aus ApiAccess erzeugen

    //private val wordRepository = WordRepository(httpClient)

    private var _currentWord = MutableLiveData("H_ _allo")
    var currentWord: LiveData<String> = _currentWord

    private var _guessedLetters = MutableLiveData<List<Char>>(emptyList())
    val guessedLetters: LiveData<List<Char>> = _guessedLetters

    private var _incorrectGuess = MutableLiveData(0)
    var incorrectGuess: LiveData<Int> = _incorrectGuess

    private var _gameWon = MutableLiveData(false)
    var gameWon: LiveData<Boolean> = _gameWon

    private val level = readLevel()
    var wordToGuess = ""

    init {
        readWord()
    }

    fun readLevel(): String {
        return savedStateHandle["EXTRA_KEY_LEVEL"] ?: "plant"
    }

    fun readWord() {
        val wordRepository = WordRepository()
        wordToGuess = wordRepository.readAll(level).value.toString()
        _currentWord.value = startingPoint(wordToGuess)
    }

    //coroutines
    /*fun readWord() {
        if (level == "plant") {
            wordToGuess = wordRepository.loadWordPlant()
        } else {
            wordToGuess = wordRepository.loadWordCountry()

        }
            _currentWord.value = startingPoint(wordToGuess)
    }*/

    fun startingPoint(solution: String): String {
        val range = solution.length
        var wordlength = ""
        for (x in 1..range) {
            wordlength += "_ "
        }
        return wordlength
    }

    fun processInput(letterInput: Char, solution: String) {
        if (letterInput.isLetter()) {

            addGuessedLetter(letterInput)

            //wenn Buchstabe vorkommt, startet chekcInputInWord
            if (letterInput in solution) {
                _currentWord.value = checkInputInWord(letterInput, solution)
                //wordToGuessTextView.text = currentScore
                if (checkFinished()) {
                    //createDialog("GEWONNEN")
                    _gameWon.value = true
                }
            } else {
                val currentIncorrect = incorrectGuess.value ?: 0
                _incorrectGuess.value = currentIncorrect + 1
            }
        }
    }

//setzt geratenen Buchstaben an der richtigen Stelle im Lösungswort ein und gibt dieses zurück

    fun checkInputInWord(
        letterInput: Char,
        solution: String,
    ): String {
        val updateWord = StringBuilder(_currentWord.value ?: "")
        for ((index, value) in solution.toCharArray().withIndex()) {
            if (value == letterInput) {
                updateWord.setCharAt(index * 2, letterInput)
            }
        }
        return updateWord.toString()
    }

    private fun checkFinished(): Boolean {
        if ("_" in _currentWord.value.orEmpty()) {
            return false
        } else {
            return true
        }
    }

    fun addGuessedLetter(letter: Char) {
        val currentList = _guessedLetters.value ?: emptyList()
        _guessedLetters.value = currentList + letter
    }
}

