package com.example.wordhunter.game

import android.content.Intent
import android.os.Bundle
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GameActivity : AppCompatActivity() {
    private val gameViewModel: GameViewModel by viewModels()

    private val LOG_TAG = "GameActivity"

    private lateinit var letterInputEditText: EditText
    private lateinit var lettersGuessedTextView: TextView
    private lateinit var failedAttemptsImageView: ImageView
    private lateinit var wordToGuessTextView: TextView
    private lateinit var categoryTextView:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        val category = gameViewModel.readCategory()
        Log.i(LOG_TAG, "Chosen Level: $category")

        wordToGuessTextView = (findViewById(R.id.tv_word_to_guess))
        failedAttemptsImageView = (findViewById(R.id.failedAttempts))
        lettersGuessedTextView = findViewById(R.id.tv_lettersguessed)
        letterInputEditText = findViewById(R.id.et_letterInput)
        categoryTextView = findViewById(R.id.tv_category)

        //Wort aus dem Repository geladen:
        lifecycleScope.launch(Dispatchers.IO) {
            gameViewModel.readWord()
            Log.i(LOG_TAG, "Wort: ${gameViewModel.wordToGuess}")

            withContext(Dispatchers.Main) {
                gameViewModel.convertWordToGuess()
                wordToGuessTextView.text = gameViewModel.currentWord.value
            }
        }

        categoryTextView.text = getString(R.string.category, category)

        val submitButton = findViewById<Button>(R.id.btn_submit)

        failedAttemptsImageView.setImageResource(R.mipmap.flower)

        gameViewModel.currentWord.observe(this)
        { currentWord ->
            Log.i(LOG_TAG, "Wort: ${gameViewModel.wordToGuess}")
            wordToGuessTextView.text = currentWord
        }

        gameViewModel.guessedLetters.observe(this)
        { guessedLetters ->
            lettersGuessedTextView.text = guessedLetters.joinToString(", ")
        }

        gameViewModel.gameWon.observe(this)
        { gameWon ->
            if (gameWon) {
                createDialog("GEWONNEN")
            }
        }

        submitButton.setOnClickListener{
            if (letterInputEditText.text.isNotEmpty() && letterInputEditText.text.toString().length == 1) {
                gameViewModel.processInput(readLetterInput(), gameViewModel.wordToGuess)
                Log.i(LOG_TAG, "Wort: ${gameViewModel.wordToGuess}")
                letterInputEditText.setText("")
                Log.i(LOG_TAG, "Button gedrückt")
            }
        }

        gameViewModel.incorrectGuess.observe(this)
        { incorrectGuess ->
            updateFailedAttemptsImage(incorrectGuess)
        }

    }

    private fun updateFailedAttemptsImage(incorrectGuess: Int) {
        val resource = when (incorrectGuess) {
            1 -> R.mipmap.flower1
            2 -> R.mipmap.flower2
            3 -> R.mipmap.flower3
            4 -> R.mipmap.flower4
            5 -> R.mipmap.flower5
            6 -> {
                createDialog("GAME OVER. LÖSUNG: ${gameViewModel.wordToGuess}")
                R.mipmap.flower6
            }

            else -> R.mipmap.flower
        }
        failedAttemptsImageView.setImageResource(resource)
    }

    //eingegebener Buchstabe wird ausgelesen, in Grossbuchstaben und von String in Char umgewandelt und zurückgegeben
    private fun readLetterInput(): Char {
        val letterInputString: String = letterInputEditText.text.toString().uppercase()
        val letterInput = letterInputString.single()
        return letterInput
    }

    override fun onStart() {
        super.onStart()
        Log.i(LOG_TAG, "Game is started")
    }


    //erstellt das Dialogfenster, wenn das Spiel aus ist
    fun createDialog(title: String) {
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

//hier noch corotutines

    /*private fun setupGame() {
    val httpClient = createHttpClient()
    lifecycleScope.launch {
        val apiWord: ApiWord =
            httpClient.get("https://www.wordgamedb.com/api/v1/words/random").body()
    }
    }*/

    /*guessedLetters observable machen -> live Data
    organisiert die Anzeige der geratenen Buchstaben
    @SuppressLint("SetTextI18n")
    private fun setGuessedLetters() {
    lettersGuessedTextView.text = getString(R.string.letters_guessed) + guessedLetters
    }*/

    /*Share-Funktion mittels Button
    shareButton.setOnClickListener {
    val implicitIntent = Intent(Intent.ACTION_SEND)
    implicitIntent.type = "text/plain"
    implicitIntent.putExtra(Intent.EXTRA_TEXT, "Welches Wort ist gesucht: $currentScore")
    val chooser = Intent.createChooser(implicitIntent, "Share your solution with")
    startActivity(chooser)
    }*/
//enthält die Logik, wenn ein falscher Buchstabe eingegeben wird
    /*private fun handleIncorrectGuess() {
    //Log.i(LOG_TAG, incorrectGuess.toString())

    if (gameViewModel.incorrectGuess == 1) {
        failedAttemptsImageView.setImageResource(R.mipmap.flower1)
    }
    if (gameViewModel.incorrectGuess == 2) {
        failedAttemptsImageView.setImageResource(R.mipmap.flower2)
    }
    if (gameViewModel.incorrectGuess == 3) {
        failedAttemptsImageView.setImageResource(R.mipmap.flower3)
    }
    if (gameViewModel.incorrectGuess == 4) {
        failedAttemptsImageView.setImageResource(R.mipmap.flower4)
    }
    if (gameViewModel.incorrectGuess == 5) {
        failedAttemptsImageView.setImageResource(R.mipmap.flower5)
    }
    if (gameViewModel.incorrectGuess == 6) {
        createDialog("GAME OVER")
    }
    }*/

    /*override fun onStop() {
    super.onStop()

    val sharedPreferences =
    this.getSharedPreferences("Amount_of_errors", MODE_PRIVATE)
    with(sharedPreferences.edit()) {
    putInt("AmountOfErrors", gameViewModel.incorrectGuess)
    apply()
    }
    }*/


    /*private fun logSharedPreferences() {
    //Die Anzahl der Fehler aus dem letzten Spiel werden aus dem Speicher aufgerufen und geloggt.
    val sharedPreferences = this.getSharedPreferences("Amount_of_errors", MODE_PRIVATE)
    val storedNumberOfErrors = sharedPreferences.getInt("AmountOfErrors", 6)
    Log.i(
        LOG_TAG,
        "Im letzten Spiel wurden " + storedNumberOfErrors + " falsche Buchstaben geraten."
    )
    }*/
}
