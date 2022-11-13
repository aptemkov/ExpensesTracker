package com.github.aptemkov.expensestracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.aptemkov.expensestracker.databinding.FragmentChartBinding
import com.github.aptemkov.expensestracker.databinding.FragmentHomeBinding

class ChartFragment : Fragment(R.layout.fragment_chart) {

    private lateinit var binding: FragmentChartBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChartBinding.bind(view)
        binding.floatingActionButton.setOnClickListener {
            val action = ChartFragmentDirections.actionNavigationChartToAddItemFragment(
                //getString(R.string.add_fragment_title),
                -1
            )
            this.findNavController().navigate(action)
        }
    }

}