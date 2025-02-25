package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val doctorButton = this.findViewById<ImageButton>(R.id.imageButtonDoctor)
        val personButton = this.findViewById<ImageButton>(R.id.imageButtonPerson)
        doctorButton.setOnClickListener {
            val intent = Intent(this, SignupDoctor::class.java)
            startActivity(intent)
        }

        personButton.setOnClickListener {
            val intent = Intent(this, SignupPatient::class.java)
            startActivity(intent)
        }

    }
}