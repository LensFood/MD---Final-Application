package com.example.lensfood1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*

class signin : AppCompatActivity() {

    private lateinit var loginUsername: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var signupRedirectText: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val signInAccount = accountTask.getResult(ApiException::class.java)
                val authCredential = GoogleAuthProvider.getCredential(signInAccount.idToken, null)
                auth.signInWithCredential(authCredential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val intent = Intent(this@signin, congrats::class.java)
                        intent.putExtra("name", user?.displayName)
                        intent.putExtra("email", user?.email)
                        intent.putExtra("username", user?.displayName)  // Or use another unique identifier if available
                        intent.putExtra("password", "N/A") // Password won't be available for Google sign in
                        Toast.makeText(this@signin, "Succses Login", Toast.LENGTH_SHORT).show()  // Pemberitahuan sukses login
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@signin, "Failed to sign in: " + task.exception, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        loginUsername = findViewById(R.id.username)
        loginPassword = findViewById(R.id.password)
        loginButton = findViewById(R.id.login)
        signupRedirectText = findViewById(R.id.signup)

        // Inisialisasi Firebase Auth dan Google Sign-In Client
        auth = FirebaseAuth.getInstance()
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, options)

        loginButton.setOnClickListener {
            if (!validateUsername() || !validatePassword()) {
                val intent = Intent(this@signin, signup::class.java)
                startActivity(intent)
            } else {
                checkUser()
            }
        }

        signupRedirectText.setOnClickListener {
            val intent = Intent(this@signin, signup::class.java)
            startActivity(intent)
        }

        // Menggunakan SignInButton alih-alih Button
        findViewById<SignInButton>(R.id.google_signin).setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            activityResultLauncher.launch(signInIntent)
        }
    }

    private fun validateUsername(): Boolean {
        val `val` = loginUsername.text.toString()
        return if (`val`.isEmpty()) {
            loginUsername.error = "Username cannot be empty"
            false
        } else {
            loginUsername.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val `val` = loginPassword.text.toString()
        return if (`val`.isEmpty()) {
            loginPassword.error = "Password cannot be empty"
            false
        } else {
            loginPassword.error = null
            true
        }
    }

    private fun checkUser() {
        val userUsername = loginUsername.text.toString().trim { it <= ' ' }
        val userPassword = loginPassword.text.toString().trim { it <= ' ' }
        val reference = FirebaseDatabase.getInstance().getReference("users")
        val checkUserDatabase = reference.orderByChild("username").equalTo(userUsername)

        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    loginUsername.error = null
                    val passwordFromDB = snapshot.child(userUsername).child("password").getValue(String::class.java)
                    if (passwordFromDB == userPassword) {
                        loginUsername.error = null
                        val nameFromDB = snapshot.child(userUsername).child("name").getValue(String::class.java)
                        val emailFromDB = snapshot.child(userUsername).child("email").getValue(String::class.java)
                        val usernameFromDB = snapshot.child(userUsername).child("username").getValue(String::class.java)

                        val intent = Intent(this@signin, MainActivity::class.java)
                        intent.putExtra("name", nameFromDB)
                        intent.putExtra("email", emailFromDB)
                        intent.putExtra("username", usernameFromDB)
                        intent.putExtra("password", passwordFromDB)
                        Toast.makeText(this@signin, "Login berhasil!", Toast.LENGTH_SHORT).show()  // Pemberitahuan sukses login
                        startActivity(intent)
                        Log.d("signin", "success")
                    } else {
                        loginPassword.error = "Invalid Credentials"
                        loginPassword.requestFocus()
                    }
                } else {
                    loginUsername.error = "User does not exist"
                    loginUsername.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}