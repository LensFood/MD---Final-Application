package com.example.lensfood1


import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lensfood1.databinding.FragmentHomesBinding
import com.example.myapplication.db.HistoryDB
import com.example.myapplication.db.RepositoryClass
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF

class homes : Fragment() {
    private lateinit var binding: FragmentHomesBinding
    private var pieChart: PieChart? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<HistoryDB>
    private lateinit var myAdapter: AdapterClass
    private lateinit var repository: RepositoryClass
    protected var tfRegular: Typeface? = null
    protected var tfLight: Typeface? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            // Inflate the layout for this fragment
            binding = FragmentHomesBinding.inflate(inflater, container, false)
            pieChart = binding.pieChart
            recyclerView = binding.recyclerView

            // Prepare Pie Chart Data and Stats (methods not implemented here)
            preparePieData()
            getStats()

            // Set layout manager for RecyclerView
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true)

            // Initialize repository
            repository = RepositoryClass(requireContext())

            // Initialize dataList
            dataList = arrayListOf()

            // Initialize AdapterClass with empty data list
            myAdapter = AdapterClass(requireContext(), repository, dataList)
            recyclerView.adapter = myAdapter


            // Load data from database
            loadDataFromDatabase()

        } catch (e: Exception) {
            Log.e("Homes", "Error in onCreateView: ${e.localizedMessage}")
        }

        return binding.root
    }

    private fun loadDataFromDatabase() {
        try {
            // Retrieve data from the repository and update the adapter
            val historyList = repository.getAllHistory()
            dataList.clear()
            dataList.addAll(historyList)
            myAdapter.notifyDataSetChanged()
        } catch (e: Exception) {
            Log.e("Homes", "Error loading data from database: ${e.localizedMessage}")
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showBottomNavigation()
    }

    private fun getStats() {
        val orderData: ArrayList<PieEntry> = ArrayList()
        val inventoryData: ArrayList<PieEntry> = ArrayList()

        // Mengisi data dengan nilai acak
        orderData.add(PieEntry((Math.random() * 100).toFloat(), "Calories burned "))
        inventoryData.add(PieEntry((Math.random() * 100).toFloat(), "Calories consumed"))


        setPieChartData(orderData, inventoryData)
    }

    private fun preparePieData() {
        pieChart!!.setUsePercentValues(true)
        pieChart!!.description.isEnabled = false
        pieChart!!.setExtraOffsets(5F, 10F, 5F, 5F)

        pieChart!!.dragDecelerationFrictionCoef = 0.95f

        pieChart!!.setCenterTextTypeface(tfLight)
        pieChart!!.centerText = generateCenterSpannableText()

        pieChart!!.isDrawHoleEnabled = true
        pieChart!!.setHoleColor(Color.WHITE)

        pieChart!!.setTransparentCircleColor(Color.WHITE)
        pieChart!!.setTransparentCircleAlpha(110)

        pieChart!!.holeRadius = 58f
        pieChart!!.transparentCircleRadius = 61f

        pieChart!!.setDrawCenterText(true)

        pieChart!!.rotationAngle = 0f
        pieChart!!.isRotationEnabled = true
        pieChart!!.isHighlightPerTapEnabled = true

        pieChart!!.animateY(1400, Easing.EaseInOutQuad)
        pieChart!!.spin(2000, 0f, 360f, Easing.EaseInOutQuad)

        val l = pieChart!!.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        pieChart!!.setEntryLabelColor(Color.WHITE)
        pieChart!!.setEntryLabelTypeface(tfRegular)
        pieChart!!.setEntryLabelTextSize(12f)
    }

    private fun setPieChartData(orderData: ArrayList<PieEntry>, inventoryData: ArrayList<PieEntry>) {
        val entries: ArrayList<PieEntry> = ArrayList()

        entries.addAll(orderData)
        entries.addAll(inventoryData)

        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        val colors: ArrayList<Int> = ArrayList()
        colors.add(ContextCompat.getColor(requireContext(), R.color.purple_500))
        colors.add(ContextCompat.getColor(requireContext(), R.color.orange))
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        data.setValueTypeface(tfLight)
        pieChart!!.data = data

        pieChart!!.highlightValues(null)
        pieChart!!.invalidate()
    }

    private fun generateCenterSpannableText(): CharSequence {
        val s = SpannableString("2000 Kcal\nof 3000 left")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 5, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 5, s.length - 4, 0)
        s.setSpan(RelativeSizeSpan(.8f), 5, s.length - 4, 0)
        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 4, s.length, 0)
        s.setSpan(RelativeSizeSpan(1.4f), s.length - 4, s.length, 0)
        return s
    }
}
