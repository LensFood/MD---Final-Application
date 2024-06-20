package com.example.lensfood1

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.lensfood1.databinding.ActivityExerciseBinding

class exercise : AppCompatActivity() {
    private lateinit var binding: ActivityExerciseBinding
    private val handler = Handler()
    private var progress = 0
    private var kalori: Int = 0
    private var durationInMillis: Long = 0
    private var exerciseName: String = ""
    private var imageFood: Bitmap? = null
    private var resumedWithoutClearReason = false // Flag untuk menandai onResume tanpa sebab yang jelas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve data from Intent
        val gifResId = intent.getIntExtra(EXTRA_GIF_ID, 0)
        val durationInMinutes = intent.getIntExtra(EXTRA_DURATION, 0)
        kalori = intent.getIntExtra(EXTRA_KALORI, 0)
        exerciseName = intent.getStringExtra(EXTRA_EXERCISE_NAME) ?: ""
        val imageFoodByteArray = intent.getByteArrayExtra(EXTRA_IMAGE_FOOD)
        imageFood = if (imageFoodByteArray != null) {
            BitmapUtils.byteArrayToBitmap(imageFoodByteArray)
        } else {
            null
        }

        Log.d(TAG, "onCreate: GIF Resource ID: $gifResId")
        Log.d(TAG, "onCreate: Duration (minutes): $durationInMinutes")
        Log.d(TAG, "onCreate: Kalori: $kalori")
        Log.d(TAG, "onCreate: Exercise Name: $exerciseName")

        // Ensure duration is calculated correctly
        durationInMillis = durationInMinutes * 60 * 1000L
        Log.d(TAG, "onCreate: Converted Duration (milliseconds): $durationInMillis")

        // Validate and set the GIF resource
        if (gifResId != 0) {
            binding.detailImage.setImageResource(gifResId)
        } else {
            Log.e(TAG, "Invalid GIF Resource ID")
            binding.detailImage.setImageResource(R.drawable.skipping) // Ensure a default image exists
        }

        // Set exercise name in the title
        binding.detailTitle.text = exerciseName
        binding.detailDesc.text = "Description of the exercise goes here."
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
            Log.d(TAG, "Resumed without clear reason, navigating to HomeFragment")
            navigateToHomeFragment()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        Log.d(TAG, "onDestroy")
    }

    private fun navigateToHomeFragment() {
        // Implementasikan navigasi ke Fragment Home menggunakan FragmentManager
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.frame_layout, homes()) // Ganti dengan ID container Fragment Home
            .commit()
    }

    private fun showCongratulationsDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_congratulations, null)
        builder.setView(dialogView)

        val dialog = builder.create()

        val okButton: Button = dialogView.findViewById(R.id.okButton)
        okButton.setOnClickListener {
            dialog.dismiss()
            setResult(RESULT_OK)
            finish()
        }

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.show()
    }

    companion object {
        private const val TAG = "ExerciseActivity"
        const val EXTRA_GIF_ID = "com.example.lensfood1.EXTRA_GIF_ID"
        const val EXTRA_DURATION = "com.example.lensfood1.EXTRA_DURATION"
        const val EXTRA_KALORI = "com.example.lensfood1.EXTRA_KALORI"
        const val EXTRA_EXERCISE_NAME = "com.example.lensfood1.EXTRA_EXERCISE_NAME"
        const val EXTRA_IMAGE_FOOD = "com.example.lensfood1.EXTRA_IMAGE_FOOD"
    }
}
