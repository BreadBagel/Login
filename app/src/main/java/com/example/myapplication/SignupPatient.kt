package com.example.myapplication

import DatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignupPatient : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_patient) // Ensure this layout exists
        dbHelper = DatabaseHelper(this)

        val usernameInput = findViewById<EditText>(R.id.signup_patient_username)
        val emailInput = findViewById<EditText>(R.id.signup_patient_email)
        val passwordInput = findViewById<EditText>(R.id.signup_patient_password)
        val registerButton = findViewById<Button>(R.id.register_patient_button)

        registerButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            } else if (dbHelper.checkPatientExists(email)) {
                Toast.makeText(this, "Email already registered!", Toast.LENGTH_SHORT).show()
            } else {
                val success = dbHelper.registerPatient(username, email, password)
                if (success) {
                    Toast.makeText(this, "Patient Registration Successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
