package com.example.lensfood1

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lensfood1.databinding.ActivityCongratsBinding
import com.example.lensfood1.databinding.ActivitySigninBinding

class congrats : AppCompatActivity() {
    private lateinit var binding: ActivityCongratsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCongratsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.next.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}