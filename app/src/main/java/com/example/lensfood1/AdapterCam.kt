package com.example.lensfood1

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.db.RepositoryClass
import pl.droidsonroids.gif.GifImageView
import java.text.SimpleDateFormat
import java.util.*

class AdapterCam(private val context: Context, private val dataList: ArrayList<DataCam>) :
    RecyclerView.Adapter<AdapterCam.ViewHolderClass>() {

    companion object {
        const val REQUEST_CODE_EXERCISE_ACTIVITY = 1001
        const val TAG = "AdapterCam" // Tag untuk logging
    }

    var onItemClick: ((DataCam) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cam, parent, false)
        Log.d(TAG, "onCreateViewHolder: ViewHolder dibuat untuk posisi $viewType")
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)

        // Set click listener untuk itemView (seluruh item)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }

        // Handle click pada GifImageView berdasarkan tipe latihan
        holder.itemView.findViewById<GifImageView>(R.id.imageCardio).setOnClickListener {
            handleGifClick(
                R.drawable.cardio,
                currentItem.kalori,
                currentItem.durasiBerlari,
                currentItem.dataTitle,
                currentItem.karbohidrat,
                currentItem.lemak.toInt(),
                currentItem.protein,
                currentItem.dataDesc
            )
        }
        holder.itemView.findViewById<GifImageView>(R.id.imageWorkout).setOnClickListener {
            handleGifClick(
                R.drawable.workout,
                currentItem.kalori,
                currentItem.durasiWorkout,
                currentItem.dataTitle,
                currentItem.karbohidrat,
                currentItem.lemak.toInt(),
                currentItem.protein,
                currentItem.dataDesc
            )
        }
        holder.itemView.findViewById<GifImageView>(R.id.imageSitUp).setOnClickListener {
            handleGifClick(
                R.drawable.situp,
                currentItem.kalori,
                currentItem.durasiSitUp,
                currentItem.dataTitle,
                currentItem.karbohidrat,
                currentItem.lemak.toInt(),
                currentItem.protein,
                currentItem.dataDesc
            )
        }
        holder.itemView.findViewById<GifImageView>(R.id.imageSkipping).setOnClickListener {
            handleGifClick(
                R.drawable.skipping,
                currentItem.kalori,
                currentItem.durasiSkipping,
                currentItem.dataTitle,
                currentItem.karbohidrat,
                currentItem.lemak.toInt(),
                currentItem.protein,
                currentItem.dataDesc
            )
        }
    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: Jumlah item = ${dataList.size}")
        return dataList.size
    }

    // ViewHolderClass untuk menghubungkan elemen UI dalam item_cam.xml
    inner class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rvTitle: TextView = itemView.findViewById(R.id.title)
        private val rvCarbo: TextView = itemView.findViewById(R.id.carbo)
        private val rvFat: TextView = itemView.findViewById(R.id.fat)
        private val rvProtein: TextView = itemView.findViewById(R.id.protein)
        private val rvCals: TextView = itemView.findViewById(R.id.cals)
        private val rvBerlari: TextView = itemView.findViewById(R.id.cardio)
        private val rvSitUp: TextView = itemView.findViewById(R.id.situp)
        private val rvWorkout: TextView = itemView.findViewById(R.id.workout)
        private val rvSkipping: TextView = itemView.findViewById(R.id.skipping)

        fun bind(dataCam: DataCam) {
            rvTitle.text = dataCam.dataTitle
            rvCarbo.text = "${dataCam.karbohidrat}g"
            rvFat.text = "${dataCam.lemak}g"
            rvProtein.text = "${dataCam.protein}g"
            rvCals.text = "${dataCam.kalori}kcal"
            rvBerlari.text = "${dataCam.durasiBerlari} menit"
            rvSitUp.text = "${dataCam.durasiSitUp} menit"
            rvWorkout.text = "${dataCam.durasiWorkout} menit"
            rvSkipping.text = "${dataCam.durasiSkipping} menit"

            // Set tag untuk mempertahankan dataCam yang terkait dengan item UI
            itemView.tag = dataCam
            Log.d(TAG, "Binding data untuk item: ${dataCam.dataTitle}")
        }
    }

    private fun launchExerciseActivityForResult(
        gifResId: Int,
        kalori: Int,
        duration: Int,
        exerciseName: String,
        imageFoodPath: String?
    ) {
        Log.d(TAG, "launchExerciseActivityForResult: Memulai ExerciseActivity dengan gifResId: $gifResId, kalori: $kalori, durasi: $duration, namaLatihan: $exerciseName")
        val intent = Intent(context, exercise::class.java).apply {
            putExtra(exercise.EXTRA_GIF_ID, gifResId)
            putExtra(exercise.EXTRA_DURATION, duration)
            putExtra(exercise.EXTRA_KALORI, kalori)
            putExtra(exercise.EXTRA_EXERCISE_NAME, exerciseName)
            putExtra(exercise.EXTRA_IMAGE_FOOD, imageFoodPath)
        }
        (context as Activity).startActivityForResult(intent, REQUEST_CODE_EXERCISE_ACTIVITY)
    }






    private fun handleGifClick(
        gifResId: Int,
        kalori: Int,
        duration: Int,
        exerciseName: String,
        karbo: Int,
        lemak: Int,
        protein: Int,
        imageFoodPath: String
    ) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        Log.d(TAG, "handleGifClick: Memproses klik GIF. Data latihan: gifResId: $gifResId, kalori: $kalori, durasi: $duration, namaLatihan: $exerciseName, tanggal: $currentDate")

        // Panggil metode insertHistory untuk menambahkan data ke database
        val repository = RepositoryClass.getInstance(context.applicationContext as Application)
        val id = repository.insertHistory(
            date = currentDate,
            imageFood = "path/to/exercise/image",
            name = exerciseName,
            karbo = karbo.toFloat(),
            lemak = lemak.toFloat(),
            protein = protein.toFloat(),
            kalori = kalori.toFloat(),
            average = kalori.toFloat(),
            imageExercise = gifResId.toString(),
            exerciseName = exerciseName,
            exerciseDuration = duration
        )
        Log.e(TAG, "image gif nya : $gifResId")

        // Logging hasil operasi penyisipan
        if (id != -1L) {
            Log.d(TAG, "Sukses menambahkan item dengan ID: $id. Data yang disisipkan: " +
                    "Tanggal: $currentDate, " +
                    "Nama: $exerciseName, " +
                    "Karbohidrat: ${karbo}g, " +
                    "Lemak: ${lemak}g, " +
                    "Protein: ${protein}g, " +
                    "Kalori: $kalori kcal, " +
                    "Durasi: $duration menit, " +
                    "Gambar $imageFoodPath")

            // Mulai exercise activity dan lanjutkan hasilnya
            launchExerciseActivityForResult(gifResId, kalori, duration, exerciseName, imageFoodPath)
        } else {
            Log.e(TAG, "Gagal menambahkan item ke database")
        }
    }

}
