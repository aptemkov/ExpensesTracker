package com.github.aptemkov.expensestracker

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.aptemkov.expensestracker.databinding.FragmentHomeBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    //NumberFormat.getCurrencyInstance().format(itemPrice)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding.floatingActionButton.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToAddItemFragment(
                -1,
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }
        initPieChart()
    }

    private fun initPieChart() {
        with(binding.pieChart) {
            // on below line we are setting user percent value,
            // setting description as enabled and offset for pie chart
            setUsePercentValues(true)
            description.isEnabled = false
            setExtraOffsets(5f, 5f, 5f, 5f)

            // on below line we are setting drag for our pie chart
            dragDecelerationFrictionCoef = 0.95f

            // on below line we are setting hole
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)

            // on below line we are setting circle color and alpha
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)

            // on below line we are setting hole radius
            holeRadius = 58f
            transparentCircleRadius = 61f

            // on below line we are setting center text
            setDrawCenterText(true)
            centerText = "SOON"

            // on below line we are setting
            // rotation for our pie chart
            rotationAngle = 0f

            // enable rotation of the pieChart by touch
            isRotationEnabled = false
            isHighlightPerTapEnabled = true

            // on below line we are setting animation for our pie chart
            animateY(1400, Easing.EaseInOutQuad)

            // on below line we are enabling our legend for pie chart
            legend.isEnabled = true
            setEntryLabelColor(Color.WHITE)
            setEntryLabelTextSize(12f)
            legend.textSize = 14f
            legend.orientation = Legend.LegendOrientation.VERTICAL
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
            // hiding show the text values on slices

            setDrawEntryLabels(false)

            // on below line we are creating array list and
            // adding data to it to display in pie chart
            val entries: ArrayList<PieEntry> = ArrayList()
            entries.add(PieEntry(70f, "Food"))
            entries.add(PieEntry(20f, "Transport"))
            entries.add(PieEntry(10f, "Entertainment"))

            // on below line we are setting pie data set
            val dataSet = PieDataSet(entries, "")

            // on below line we are setting icons.
            dataSet.setDrawIcons(false)

            // on below line we are setting slice for pie
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f

            // add a lot of colors to list
            val colors: ArrayList<Int> = ArrayList()
            colors.add(resources.getColor(R.color.purple_200))
            colors.add(resources.getColor(androidx.appcompat.R.color.material_blue_grey_800))
            colors.add(resources.getColor(androidx.appcompat.R.color.material_deep_teal_500))

            // on below line we are setting colors.
            dataSet.colors = colors

            // on below line we are setting pie data set
            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(9f)
            data.setValueTypeface(Typeface.DEFAULT_BOLD)
            data.setValueTextColor(Color.WHITE)
            this.data = data

            // undo all highlights
            highlightValues(null)

            // loading chart
            invalidate()

        }
    }

}
