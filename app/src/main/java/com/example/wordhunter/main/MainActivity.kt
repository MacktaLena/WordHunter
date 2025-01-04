package com.example.wordhunter.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wordhunter.R
import com.example.wordhunter.game.GameActivity

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val startButtonEasy = findViewById<Button>(R.id.btn_start_easy)
        val startButtonDifficult = findViewById<Button>(R.id.btn_start_diff)

        startButtonEasy.setOnClickListener {
            val startGameActivity = Intent(this, GameActivity::class.java)
            startGameActivity.putExtra("EXTRA_KEY_CATEGORY", "plants")
            startActivity(startGameActivity)
        }

        startButtonDifficult.setOnClickListener {
            val startGameActivity = Intent(this, GameActivity::class.java)
            startGameActivity.putExtra("EXTRA_KEY_CATEGORY", "countries")
            startActivity(startGameActivity)
        }

    }
}