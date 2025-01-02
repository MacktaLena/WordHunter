package com.example.wordhunter.game

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wordhunter.main.MainActivity
import com.example.wordhunter.R
import com.example.wordhunter.repository.ApiWord
import com.example.wordhunter.repository.createHttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {
    private val gameViewModel: GameViewModel by viewModels()

    val LOG_TAG = "GameActivity"

    lateinit var letterInputEditText: EditText
    val guessedLetters = mutableListOf<Char>()
    lateinit var lettersGuessedTextView: TextView
    lateinit var failedAttemptsImageView: ImageView
    lateinit var wordToGuessTextView: TextView
    lateinit var currentScore: String
    var incorrectGuess = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        Log.i(LOG_TAG, "Game on Create")
        logSharedPreferences()



        val level = gameViewModel.readLevel()
        Log.i(LOG_TAG, "Chosen Level: $level")

        wordToGuessTextView = (findViewById(R.id.tv_word_to_guess))
        failedAttemptsImageView = (findViewById(R.id.failedAttempts))
        lettersGuessedTextView = findViewById(R.id.tv_lettersguessed)
        letterInputEditText = findViewById(R.id.et_letterInput)

        val secretWord = chooseWord(level)
        val submitButton = findViewById<Button>(R.id.btn_submit)
        val shareButton = findViewById<Button>(R.id.btn_share)

        currentScore = startingPoint(secretWord)
        wordToGuessTextView.text = currentScore
        failedAttemptsImageView.setImageResource(R.mipmap.flower)

        //TextchangeListener logged, wenn der Buchstabe, der eingegeben wird, sich ändert und gibt die Änderung, nachdem sie durchgeführt wurde, in der Konsole aus
        letterInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Log.i(LOG_TAG, "before" + s.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Log.i(LOG_TAG, "on" + s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(LOG_TAG, "after" + s.toString())
            }
        })

        submitButton.setOnClickListener {
            if (letterInputEditText.text.isNotEmpty() && letterInputEditText.text.toString().length == 1) {
                processInput(readLetterInput(), secretWord)
                Log.i(LOG_TAG, "Button gedrückt")
            }
        }

        //Share-Funktion mittels Button
        shareButton.setOnClickListener {
            val implicitIntent = Intent(Intent.ACTION_SEND)
            implicitIntent.type = "text/plain"
            implicitIntent.putExtra(Intent.EXTRA_TEXT, "Welches Wort ist gesucht: $currentScore")
            val chooser = Intent.createChooser(implicitIntent, "Share your solution with")
            startActivity(chooser)
        }
    }

    //Wort aus Liste easyWords bzw. difficultWords wird random ausgewählt, in Großbuchstaben umgewandelt und zurückgegeben
    private fun chooseWord(level: String): String {
        var chosenWord = "Default"
        val easyWords = listOf("nest", "mauer", "schnee", "herr", "stern", "reise")
        val difficultWords =
            listOf("zucchini", "krautkopf", "feuerwehr", "terrasse", "brillant", "recycling")

        if (level == "plant") {
            chosenWord = easyWords.random().uppercase()
        } else {
            chosenWord = difficultWords.random().uppercase()
        }
        return chosenWord
    }

    //für jeden Buchstaben des ausgewählten Wortes wird ein _ erzeugt und zurückgegeben
    private fun startingPoint(solution: String): String {
        val range = solution.length
        var wordlength = ""
        for (x in 1..range) {
            wordlength += "_ "
        }
        return wordlength
    }

    //Gibt Buchstabe zur Liste geratener Buchstaben und überprüft, ob eingegebener Buchstabe im Lösungswort vorkommt,
    private fun processInput(letterInput: Char, solution: String) {
        if (letterInput.isLetter()) {

            guessedLetters.add(letterInput)
            setGuessedLetters()
            letterInputEditText.setText("")


            //wenn Buchstabe vorkommt, startet chekcInputInWord
            if (letterInput in solution) {
                currentScore = checkInputInWord(letterInput, solution)
                wordToGuessTextView.text = currentScore
                if (checkFinished()) {
                    createDialog("GEWONNEN")
                }

            } else {
                handleIncorrectGuess()
            }
        }
    }

    //schaut, ob Lösungswort komplett
    private fun checkFinished(): Boolean {
        if ("_" in currentScore) {
            return false
        } else {
            return true
        }
    }


//setzt geratenen Buchstaben an der richtigen Stelle im Lösungswort ein und gibt dieses zurück

    private fun checkInputInWord(
        letterInput: Char,
        solution: String,
    ): String {
        val currentScore2 = StringBuilder(currentScore)
        for ((index, value) in solution.toCharArray().withIndex()) {
            if (value == letterInput) {
                currentScore2.setCharAt(index * 2, letterInput)
            }
        }
        return currentScore2.toString()
    }

    //enthält die Logik, wenn ein falscher Buchstabe eingegeben wird
    private fun handleIncorrectGuess() {
        incorrectGuess += 1
        Log.i(LOG_TAG, incorrectGuess.toString())

        if (incorrectGuess == 1) {
            failedAttemptsImageView.setImageResource(R.mipmap.flower1)
        }
        if (incorrectGuess == 2) {
            failedAttemptsImageView.setImageResource(R.mipmap.flower2)
        }
        if (incorrectGuess == 3) {
            failedAttemptsImageView.setImageResource(R.mipmap.flower3)
        }
        if (incorrectGuess == 4) {
            failedAttemptsImageView.setImageResource(R.mipmap.flower4)
        }
        if (incorrectGuess == 5) {
            failedAttemptsImageView.setImageResource(R.mipmap.flower5)
        }
        if (incorrectGuess == 6) {
            createDialog("GAME OVER")
        }
    }

    //organisiert die Anzeige der geratenen Buchstaben
    @SuppressLint("SetTextI18n")
    private fun setGuessedLetters() {
        lettersGuessedTextView.text = getString(R.string.letters_guessed) + guessedLetters
    }

    //eingegebener Buchstabe wird ausgelesen, in Grossbuchstaben und von String in Char umgewandelt und zurückgegeben
    private fun readLetterInput(): Char {
        val letterInputString: String = letterInputEditText.text.toString().uppercase()
        val letterInput = letterInputString.single()
        return letterInput
    }

    //erstellt das Dialogfenster, wenn das Spiel aus ist
    private fun createDialog(title: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@GameActivity)
            .setMessage("Noch ein Spiel?")
            .setTitle(title)
            .setPositiveButton("Ja") { dialog, which ->

                val startMainActivity = Intent(this, MainActivity::class.java)
                startActivity(startMainActivity)

            }
            .setNegativeButton("Nein") { dialog, which ->
                finishAffinity()
            }
            .setCancelable(false)

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun logSharedPreferences() {
        //Die Anzahl der Fehler aus dem letzten Spiel werden aus dem Speicher aufgerufen und geloggt.
        val sharedPreferences = this.getSharedPreferences("Amount_of_errors", MODE_PRIVATE)
        val storedNumberOfErrors = sharedPreferences.getInt("AmountOfErrors", 6)
        Log.i(
            LOG_TAG,
            "Im letzten Spiel wurden " + storedNumberOfErrors + " falsche Buchstaben geraten."
        )
    }

    override fun onStart() {
        super.onStart()
        Log.i(LOG_TAG, "Game is started")
    }

    override fun onStop() {
        super.onStop()

        val sharedPreferences =
            this.getSharedPreferences("Amount_of_errors", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("AmountOfErrors", incorrectGuess)
            apply()
        }
    }

    //hier noch corotutines

    private fun setupGame() {
        val httpClient = createHttpClient()
        lifecycleScope.launch {
            val apiWord: ApiWord =
                httpClient.get("https://www.wordgamedb.com/api/v1/words/random").body()
        }
    }

}