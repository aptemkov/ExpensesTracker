package com.github.aptemkov.expensestracker

import android.content.res.Resources.Theme
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.aptemkov.expensestracker.databinding.FragmentHomeBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import kotlin.math.absoluteValue

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            (activity?.application as ExpensesApplication).database.itemDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.floatingActionButton.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToAddItemFragment(
                -1,
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }

        viewModel.allItems.observe(this.viewLifecycleOwner) { items->
            items.let {

                with(viewModel) {
                    getFormattedWithCurrencyValue(viewModel.getTotalExpense(items))
                        .also { binding.tvTotalExpense.text = it }

                    getFormattedWithCurrencyValue(viewModel.getTotalCouldSave(items))
                        .also { binding.tvCouldSave.text = it }

                    getFormattedWithCurrencyValue(viewModel.getTotalIncome(items))
                        .also { binding.tvTotalIncome.text = it }

                    getFormattedWithCurrencyValue(viewModel.getTotalBalance(items))
                        .also { binding.tvTotalBalance.text = it }

                    initPieChart(getMapForPieChart(items))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initPieChart(mapForPieChart: Map<String, Double>) {


        val mainColor = binding.tvCouldSave.currentTextColor
        with(binding.pieChart) {
            // on below line we are setting user percent value,
            setUsePercentValues(true)
            // setting description as enabled and offset for pie chart
            description.isEnabled = false
            setExtraOffsets(5f, 5f, 5f, 5f)

            // on below line we are setting drag for our pie chart
            dragDecelerationFrictionCoef = 0.95f

            // on below line we are setting hole
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)

            // on below line we are setting circle color and alpha
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)

            // on below line we are setting hole radius
            holeRadius = 58f
            transparentCircleRadius = 61f

            // on below line we are setting center text
            setDrawCenterText(true)
            centerText = "SOON"
            setCenterTextColor(mainColor)

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
            setEntryLabelColor(mainColor)
            setEntryLabelTextSize(12f)
            legend.textSize = 14f
            legend.textColor = mainColor
            legend.orientation = Legend.LegendOrientation.VERTICAL
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
            // hiding show the text values on slices

            setDrawEntryLabels(false)

            // on below line we are creating array list and
            // adding data to it to display in pie chart
            val entries: List<PieEntry> = mapForPieChart.map {PieEntry(it.value.toFloat(), it.key)}

            // on below line we are setting pie data set
            val dataSet = PieDataSet(entries, "")

            // on below line we are setting icons.
            dataSet.setDrawIcons(false)

            // on below line we are setting slice for pie
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f

            // add a lot of colors to list
            val colors: List<Int> = listOf(
            resources.getColor(R.color.pie_chart_color1),
            resources.getColor(R.color.pie_chart_color2),
            resources.getColor(R.color.pie_chart_color3),
            resources.getColor(R.color.pie_chart_color4),
            resources.getColor(R.color.pie_chart_color5),
            resources.getColor(R.color.pie_chart_color6),
            resources.getColor(R.color.pie_chart_color7),
            resources.getColor(R.color.pie_chart_color8),
            resources.getColor(R.color.pie_chart_color9),
            resources.getColor(R.color.pie_chart_color10),
            resources.getColor(R.color.pie_chart_color11),
            resources.getColor(R.color.pie_chart_color12),
            resources.getColor(R.color.pie_chart_color13),
            resources.getColor(R.color.pie_chart_color14),
            resources.getColor(R.color.pie_chart_color15),
            resources.getColor(R.color.pie_chart_color16),
            resources.getColor(R.color.pie_chart_color17),
            )
            // on below line we are setting colors.
            dataSet.colors = colors

            // on below line we are setting pie data set
            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(0f)
            data.setValueTypeface(Typeface.DEFAULT_BOLD)
            data.setValueTextColor(mainColor)
            this.data = data

            // undo all highlights
            highlightValues(null)

            // loading chart
            invalidate()

        }
    }

}
