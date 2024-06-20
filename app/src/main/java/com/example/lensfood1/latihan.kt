package com.example.lensfood1

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pl.droidsonroids.gif.GifImageView

class latihan : AppCompatActivity() {

    private lateinit var gifImageView: ImageView
    private lateinit var timerTextTextView: TextView
    private lateinit var countdownTimer: CountDownTimer
    private lateinit var handler: Handler
    private lateinit var updateTextTask: Runnable

    private var millisUntilFinished: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latihan)

        gifImageView = findViewById(R.id.imageGIF)
        timerTextTextView = findViewById(R.id.timerText)




        val gifId = intent.getStringExtra(EXTRA_GIF)
        Log.d("durasi", "nilai GIF yang diterima di latihan: $gifId")
        when (gifId) {
            "2131230987" -> {
                gifImageView.setImageResource(R.drawable.skipping)
            }
            "2131230986" -> {
                gifImageView.setImageResource(R.drawable.situp)
            }
            "2131230872" -> {
                gifImageView.setImageResource(R.drawable.cardio)
            }
            "2131231010" -> {
                gifImageView.setImageResource(R.drawable.workout)
            }
            else -> {
                gifImageView.setImageResource(R.drawable.skipping) // Default image if no match found
            }
        }

        val durationMinutes = intent.getLongExtra(EXTRA_DURATION, 0)
        val durationMillis = durationMinutes * 60 * 1000 // Convert minutes to milliseconds

        // Initialize handler and runnable for updating timerTextTextView
        handler = Handler()
        updateTextTask = object : Runnable {
            override fun run() {
                // Calculate remaining time in milliseconds, seconds, and minutes
                val millis = millisUntilFinished % 1000
                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / (1000 * 60)) % 60
                val millisFormatted = String.format("%03d", millis)
                timerTextTextView.text = String.format("%02d:%02d:%s", minutes, seconds, millisFormatted)
                handler.postDelayed(this, 1) // Update text every millisecond
            }
        }

        // Start countdown timer
        startCountdownTimer(durationMillis)
    }

    private fun startCountdownTimer(durationMillis: Long) {
        countdownTimer = object : CountDownTimer(durationMillis, 1) {
            override fun onTick(millisUntilFinished: Long) {
                this@latihan.millisUntilFinished = millisUntilFinished
            }

            override fun onFinish() {
                timerTextTextView.text = "00:00:000"
                showCongratulationsDialog()
            }
        }

        countdownTimer.start()

        // Start updating timerTextTextView every millisecond
        handler.post(updateTextTask)
    }

    private fun showCongratulationsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_congratulations)
        dialog.setCancelable(false)

        val closeButton = dialog.findViewById<Button>(R.id.okButton)
        closeButton.setOnClickListener {
            dialog.dismiss()
            finish() // Optionally, close activity after dismissing dialog
        }

        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove any pending callbacks to prevent memory leaks
        handler.removeCallbacks(updateTextTask)
    }

    companion object {
        const val EXTRA_GIF = "extra_gif_id"
        const val EXTRA_DURATION = "extra_duration"
    }
}
