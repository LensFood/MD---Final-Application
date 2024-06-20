package com.example.lensfood1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class signup : AppCompatActivity() {

    private lateinit var signupName: EditText
    private lateinit var signupUsername: EditText
    private lateinit var signupEmail: EditText
    private lateinit var signupPassword: EditText
    private lateinit var loginRedirectText: TextView
    private lateinit var signupButton: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signupName = findViewById(R.id.name)
        signupEmail = findViewById(R.id.email)
        signupUsername = findViewById(R.id.username)
        signupPassword = findViewById(R.id.password)
        loginRedirectText = findViewById(R.id.login)
        signupButton = findViewById(R.id.btnsignup)

        signupButton.setOnClickListener {
            val name = signupName.text.toString().trim()
            val email = signupEmail.text.toString().trim()
            val username = signupUsername.text.toString().trim()
            val password = signupPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@signup, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            } else {
                database = FirebaseDatabase.getInstance()
                reference = database.getReference("users")

                val helperClass = HelperFB(name, email, username, password)
                reference.child(username).setValue(helperClass)

                Toast.makeText(this@signup, "You have signed up successfully!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@signup, signin::class.java)
                startActivity(intent)
            }
        }

        loginRedirectText.setOnClickListener {
            val intent = Intent(this@signup, signin::class.java)
            startActivity(intent)
        }
    }
}
