package com.example.websitetracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_search)

        val searchEditText = findViewById<TextInputEditText>(R.id.searchEditText)
        val button = findViewById<Button>(R.id.searchButton)

        button.setOnClickListener {
            val input = searchEditText.text.toString()
            val url = if (input.startsWith("https://")) input else "https://".plus(input)
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(webIntent)
        }
    }
}
