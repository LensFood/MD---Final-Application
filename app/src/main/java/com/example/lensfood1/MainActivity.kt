package com.example.lensfood1

import android.Manifest
import android.content.Intent

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.lensfood1.databinding.ActivityMainBinding
import com.yalantis.ucrop.UCrop


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }


    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.myFab.setOnClickListener { startCamera() }
        binding.myFab2.setOnClickListener {
            val intent = Intent(this, chatbot::class.java)
            startActivity(intent)
        }

        // Initialize with HomeFragment
        replaceFragment(homes())

        // Set up BottomNavigationView
        binding.myBottomNavigationView.setBackground(null)
        binding.myBottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(homes())
                R.id.shorts -> replaceFragment(shorts())
                R.id.subscriptions -> replaceFragment(subscriptions())
                R.id.library -> replaceFragment(library())
            }
            true
        }
        binding.myFab.setOnClickListener { startCamera() }
    }






    private fun analyzeImage(uri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            if (bitmap != null) {
                val foodClassifier = ObjectDetectorHelper(this)

                val detectionResults = foodClassifier.runInference(bitmap)
                val dataList = ArrayList<DataCam>()

                detectionResults.forEach { result ->
                    val dataCam = DataCam(
                        dataDesc = result.dataDesc, // Placeholder image ID or set to null
                        dataTitle = result.dataTitle,
                        score = result.score,
                        karbohidrat = result.karbohidrat, // Example value, replace with actual data
                        kalori = result.kalori, // Example value, replace with actual data
                        lemak = result.lemak, // Example value, replace with actual data
                        protein = result.protein, // Example value, replace with actual data
                        durasiBerlari = result.durasiBerlari,
                        durasiSitUp = result.durasiSitUp,
                        durasiWorkout = result.durasiWorkout,
                        durasiSkipping = result.durasiSkipping
                    )
                    dataList.add(dataCam)
                }

                // Panggil showImageWithDetection dengan hasil analisis dan bitmap asli
                showImageWithDetection(bitmap, dataList)
            } else {
                showToast("Failed to decode image from URI")
            }
        } catch (e: Exception) {
            Log.e("AnalyzeImage", "Error analyzing image: ${e.message}")
            showToast("Error analyzing image: ${e.message}")
        }
    }


    private fun showImageWithDetection(originalBitmap: Bitmap, dataList: ArrayList<DataCam>) {
        runOnUiThread {
            showBottomDialog(dataList, originalBitmap)
        }
    }

    private fun showBottomDialog(dataList: ArrayList<DataCam>, image: Bitmap) {
        BottomSheetF.show(supportFragmentManager, dataList, image)
    }




    private fun startCamera() {
        if (!allPermissionsGranted()) {
            showToast("Camera permission is required")
            return
        }

        currentImageUri = getImageUri(this)
        if (currentImageUri != null) {
            Log.d("MainActivity", "Starting camera with URI: $currentImageUri")
            launcherIntentCamera.launch(currentImageUri)
        } else {
            showToast("Failed to generate image URI")
        }
    }








    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }





    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri?.let { uri ->
                startUCropActivity(uri)
            }
        } else {
            showToast("Failed to capture image")
        }
    }





    private fun startUCropActivity(uri: Uri) {
        val currentImageUri = currentImageUri ?: return

        Log.d("MainActivity", "Starting UCrop with source URI: $uri and destination URI: $currentImageUri")

        val uCropIntent = UCrop.of(uri, currentImageUri)
            .withAspectRatio(1.0f, 1.0f)
            .getIntent(this@MainActivity)

        startUCrop.launch(uCropIntent)
    }


    private val startUCrop = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            Log.d("MainActivity", "UCrop result URI: $resultUri")
            if (resultUri != null) {
                analyzeImage(resultUri)
            } else {
                showToast("Cropping failed")
            }
        } else {
            Log.d("MainActivity", "UCrop failed or canceled")
        }
    }





    // Method to show BottomNavigationView
    fun showBottomNavigation() {
        binding.myBottomNavigationView.visibility = View.VISIBLE
    }



    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
