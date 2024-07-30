package com.example.redoproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton: Button = findViewById(R.id.searchButton)
        val localNewsButton: Button = findViewById(R.id.localNewsButton)
        val topHeadlinesButton: Button = findViewById(R.id.topHeadlinesButton)

        searchButton.setOnClickListener {
            val searchTerm = findViewById<EditText>(R.id.searchEditText).text.toString()
            val intent = Intent(this, SourceSelectionActivity::class.java)
            intent.putExtra("searchTerm", searchTerm)
            startActivity(intent)
        }


        localNewsButton.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        topHeadlinesButton.setOnClickListener {
            startActivity(Intent(this, TopHeadlinesActivity::class.java))
        }
    }
}
