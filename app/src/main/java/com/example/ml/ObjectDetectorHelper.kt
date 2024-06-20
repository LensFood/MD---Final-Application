package com.example.lensfood1

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.lensfood1.ml.Detect
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ObjectDetectorHelper(private val context: Context) {


    private fun getDurasiBerlari(foodName: String): Int {
        return when (foodName) {
            "ayam bakar" -> 16
            "ayam goreng" -> 25
            "bakso" -> 18
            "bakwan" -> 20
            "batagor" -> 29
            "bihun" -> 12
            "capcay" -> 10
            "gado-gado" -> 38
            "ikan goreng" -> 24
            "kerupuk" -> 7
            "martabak telur" -> 38
            "mie" -> 19
            "nasi goreng" -> 34
            "nasi putih" -> 20
            "nugget" -> 25
            "opor ayam" -> 30
            "pempek" -> 26
            "rendang" -> 31
            "roti" -> 20
            "sate" -> 20
            "sosis" -> 15
            "soto" -> 15
            "steak" -> 27
            "tahu" -> 8
            "telur" -> 7
            "tempe" -> 20
            "terong balado" -> 15
            "tumis kangkung" -> 4
            "udang" -> 10
            else -> 0
        }
    }

    private fun getDurasiSitUp(foodName: String): Int {
        return when (foodName) {
            "ayam bakar" -> 27
            "ayam goreng" -> 41
            "bakso" -> 30
            "bakwan" -> 33
            "batagor" -> 48
            "bihun" -> 20
            "capcay" -> 17
            "gado-gado" -> 63
            "ikan goreng" -> 40
            "kerupuk" -> 11
            "martabak telur" -> 63
            "mie" -> 32
            "nasi goreng" -> 57
            "nasi putih" -> 34
            "nugget" -> 42
            "opor ayam" -> 50
            "pempek" -> 43
            "rendang" -> 52
            "roti" -> 34
            "sate" -> 34
            "sosis" -> 25
            "soto" -> 25
            "steak" -> 45
            "tahu" -> 13
            "telur" -> 12
            "tempe" -> 34
            "terong balado" -> 25
            "tumis kangkung" -> 7
            "udang" -> 17
            else -> 0
        }
    }

    private fun getDurasiWorkout(foodName: String): Int {
        return when (foodName) {
            "ayam bakar" -> 20
            "ayam goreng" -> 31
            "bakso" -> 23
            "bakwan" -> 25
            "batagor" -> 36
            "bihun" -> 12
            "capcay" -> 15
            "gado-gado" -> 47
            "ikan goreng" -> 30
            "kerupuk" -> 8
            "martabak telur" -> 48
            "mie" -> 24
            "nasi goreng" -> 43
            "nasi putih" -> 26
            "nugget" -> 31
            "opor ayam" -> 38
            "pempek" -> 33
            "rendang" -> 39
            "roti" -> 25
            "sate" -> 25
            "sosis" -> 19
            "soto" -> 19
            "steak" -> 34
            "tahu" -> 10
            "telur" -> 9
            "tempe" -> 25
            "terong balado" -> 19
            "tumis kangkung" -> 5
            "udang" -> 12
            else -> 0
        }
    }

    private fun getDurasiSkipping(foodName: String): Int {
        return when (foodName) {
            "ayam bakar" -> 14
            "ayam goreng" -> 21
            "bakso" -> 15
            "bakwan" -> 17
            "batagor" -> 24
            "bihun" -> 10
            "capcay" -> 8
            "gado-gado" -> 31
            "ikan goreng" -> 20
            "kerupuk" -> 5
            "martabak telur" -> 32
            "mie" -> 16
            "nasi goreng" -> 28
            "nasi putih" -> 17
            "nugget" -> 21
            "opor ayam" -> 25
            "pempek" -> 22
            "rendang" -> 26
            "roti" -> 17
            "sate" -> 17
            "sosis" -> 13
            "soto" -> 13
            "steak" -> 23
            "tahu" -> 6
            "telur" -> 6
            "tempe" -> 17
            "terong balado" -> 13
            "tumis kangkung" -> 3
            "udang" -> 8
            else -> 0
        }
    }



    private val classNames = listOf(
        "ayam bakar", "ayam goreng", "bakso", "bakwan", "batagor", "bihun", "capcay", "gado-gado",
        "ikan goreng", "kerupuk", "martabak telur", "mie", "nasi goreng", "nasi putih", "nugget",
        "opor ayam", "pempek", "rendang", "roti", "sate", "sosis", "soto", "steak", "tahu", "telur",
        "tempe", "terong balado", "tumis kangkung", "udang"
    )

    // Data nutrisi untuk setiap makanan
    private val nutritionData = mapOf(
        "ayam bakar" to Nutrition(0, 164, 6.5, 24),
        "ayam goreng" to Nutrition(10, 246, 12.0, 27),
        "bakso" to Nutrition(7, 180, 10.0, 16),
        "bakwan" to Nutrition(15, 200, 10.0, 5),
        "batagor" to Nutrition(25, 289, 15.0, 11),
        "bihun" to Nutrition(27, 120, 0.5, 2),
        "capcay" to Nutrition(9, 100, 6.0, 4),
        "gado-gado" to Nutrition(32, 376, 22.0, 14),
        "ikan goreng" to Nutrition(0, 240, 13.0, 26),
        "kerupuk" to Nutrition(12, 65, 3.5, 2),
        "martabak telur" to Nutrition(30, 380, 20.0, 14),
        "mie" to Nutrition(40, 190, 7.0, 5),
        "nasi goreng" to Nutrition(45, 340, 15.0, 10),
        "nasi putih" to Nutrition(45, 204, 0.5, 4),
        "nugget" to Nutrition(15, 250, 18.0, 10),
        "opor ayam" to Nutrition(5, 300, 25.0, 15),
        "pempek" to Nutrition(40, 260, 10.0, 10),
        "rendang" to Nutrition(5, 312, 26.0, 18),
        "roti" to Nutrition(40, 200, 2.0, 7),
        "sate" to Nutrition(5, 200, 12.0, 20),
        "sosis" to Nutrition(1, 150, 12.0, 7),
        "soto" to Nutrition(8, 150, 5.0, 12),
        "steak" to Nutrition(0, 271, 20.0, 23),
        "tahu" to Nutrition(2, 76, 4.0, 8),
        "telur" to Nutrition(1, 70, 5.0, 6),
        "tempe" to Nutrition(10, 200, 11.0, 19),
        "terong balado" to Nutrition(10, 150, 10.0, 3),
        "tumis kangkung" to Nutrition(5, 40, 2.0, 2),
        "udang" to Nutrition(1, 99, 1.5, 20)
    )

    data class Nutrition(val karbohidrat: Int, val kalori: Int, val lemak: Double, val protein: Int)
    data class DetectionResult(val score: Float, val className: String, val nutrition: Nutrition)

    // Fungsi untuk memproses gambar
    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        // Resize gambar ke ukuran yang diharapkan model
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

        val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        // Konversi bitmap ke array int
        val intValues = IntArray(224 * 224)
        resizedBitmap.getPixels(intValues, 0, 224, 0, 0, 224, 224)

        // Normalisasi nilai pixel
        for (value in intValues) {
            byteBuffer.putFloat(((value shr 16 and 0xFF) / 255.0f))
            byteBuffer.putFloat(((value shr 8 and 0xFF) / 255.0f))
            byteBuffer.putFloat(((value and 0xFF) / 255.0f))
        }

        return byteBuffer
    }

    // Fungsi untuk menjalankan inferensi pada gambar
    fun runInference(bitmap: Bitmap): List<DataCam> {
        // Muat model TFLite
        val model = Detect.newInstance(context)

        // Proses gambar menjadi input tensor
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(preprocessImage(bitmap))

        // Jalankan inferensi
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        // Ambil hasil dari output tensor
        val predictions = outputFeature0.floatArray

        // Urutkan berdasarkan confidence dan ambil 5 terbesar
        val top5Results = predictions.mapIndexed { index, score ->
            val className = classNames[index]
            DetectionResult(score, className, nutritionData[className]!!)
        }.sortedByDescending { it.score }.take(5)

        // Buat instance DataCam untuk setiap hasil prediksi
        val dataCamResults = top5Results.map { result ->
            DataCam(
                dataTitle = result.className,
                dataDesc = "",
                score = result.score,
                karbohidrat = result.nutrition.karbohidrat,
                kalori = result.nutrition.kalori,
                lemak = result.nutrition.lemak,
                protein = result.nutrition.protein,
                durasiBerlari = getDurasiBerlari(result.className),
                durasiSitUp = getDurasiSitUp(result.className),
                durasiWorkout = getDurasiWorkout(result.className),
                durasiSkipping = getDurasiSkipping(result.className)
            )
        }

        // Tampilkan di log
        top5Results.forEachIndexed { index, detection ->
            Log.d("ObjectDetector", "Top Detection #$index: Class=${detection.className}, Score=${detection.score}, Karbohidrat=${detection.nutrition.karbohidrat}g, Kalori=${detection.nutrition.kalori}kcal, Lemak=${detection.nutrition.lemak}g, Protein=${detection.nutrition.protein}g")
        }

        model.close()

        return dataCamResults
    }
}
