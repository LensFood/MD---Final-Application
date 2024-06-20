package com.example.lensfood1
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.formatter.ValueFormatter

class shorts : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var lineChart: LineChart

    // Data hardcoded untuk satu minggu
    private val days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    private val consumedCalories = floatArrayOf(2100f, 1800f, 2200f, 2000f, 1900f, 1800f, 1700f) // Kalori yang dikonsumsi
    private val burnedCalories = floatArrayOf(500f, 600f, 400f, 700f, 650f, 600f, 500f) // Kalori yang dibakar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shorts, container, false)

        // Inisialisasi LineChart
        lineChart = view.findViewById(R.id.lineChart)
        setupLineChart()

        return view
    }

    private fun setupLineChart() {
        // Part1: Membuat daftar entry data untuk kalori yang dikonsumsi dan dibakar
        val consumedEntries = ArrayList<Entry>().apply {
            for (i in consumedCalories.indices) {
                add(Entry((i + 1).toFloat(), consumedCalories[i]))
            }
        }

        val burnedEntries = ArrayList<Entry>().apply {
            for (i in burnedCalories.indices) {
                add(Entry((i + 1).toFloat(), burnedCalories[i]))
            }
        }

        // Part2: Membuat dataset dari entry data
        val consumedDataSet = LineDataSet(consumedEntries, "Calories Consumed").apply {
            setDrawValues(false)
            setDrawFilled(true)
            lineWidth = 3f
            fillColor = resources.getColor(R.color.light_blue)
            fillAlpha = 255
            color = resources.getColor(R.color.purple_200)
        }

        val burnedDataSet = LineDataSet(burnedEntries, "Calories Burned").apply {
            setDrawValues(false)
            setDrawFilled(true)
            lineWidth = 3f
            fillColor = resources.getColor(R.color.light_orange)
            fillAlpha = 100
            color = resources.getColor(R.color.orange)
        }

        // Part3: Mengatur data untuk LineChart
        lineChart.apply {
            data = LineData(consumedDataSet, burnedDataSet)
            xAxis.labelRotationAngle = 0f
            axisRight.isEnabled = false
            xAxis.axisMaximum = days.size.toFloat() + 0.1f
            xAxis.granularity = 1f // Menentukan bahwa nilai akan ditampilkan dengan interval 1 (untuk setiap hari)
            setTouchEnabled(true)
            setPinchZoom(true)
            isDragEnabled = true // Mengizinkan dragging (scroll)
            setScaleEnabled(false) // Tidak mengizinkan scaling untuk fokus pada scrolling
            description.text = "Daily Calories"
            setNoDataText("No data available")
            animateX(1800, Easing.EaseInExpo)
            extraBottomOffset = 10f // Memberikan ruang tambahan untuk label XAxis di bagian bawah
        }

        // Part4: Menyiapkan XAxis dengan formatter untuk menampilkan hari dan menempatkan di atas grafik
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt() - 1
                return if (index >= 0 && index < days.size) {
                    days[index]
                } else {
                    ""
                }
            }
        }
        xAxis.granularity = 1f // Menentukan bahwa nilai akan ditampilkan dengan interval 1 (untuk setiap hari)
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance(param1: String, param2: String, consumedCalories: FloatArray, burnedCalories: FloatArray) =
            shorts().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putFloatArray("consumedCalories", consumedCalories)
                    putFloatArray("burnedCalories", burnedCalories)
                }
            }
    }



}
