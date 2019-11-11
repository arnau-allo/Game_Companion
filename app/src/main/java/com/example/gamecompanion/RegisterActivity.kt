package com.example.gamecompanion

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        loginButton.setOnClickListener {
            val username = usernameEditText.text?.toString().orEmpty()
            val email = emailEditText.text?.toString().orEmpty()
            val password = passwordEditText.text?.toString().orEmpty()

            if(username.isEmpty()){
                Toast.makeText(this, "Username Required", Toast.LENGTH_LONG).show()
                usernameEditText.error = "Username Required"
                return@setOnClickListener
            }

            if(email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(this, "Invalid Email", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(password.isBlank() || !isPasswordValid(password)){
                Toast.makeText(this, "Password requires letters and numbers and at least 4 characters", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener { authResult ->
                    Toast.makeText(this,"Success creating new user",Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
        }
    }
}

private fun isPasswordValid(password: String):Boolean{
    if(password.length<4){
        return false
    }

    var digit:Boolean = false
    var character:Boolean = false

    for (letter in password) {
        if(letter.isDigit()){
            digit = true
        }
        if(letter.isLetter()){
            character = true
        }
    }

    return  digit && character
}