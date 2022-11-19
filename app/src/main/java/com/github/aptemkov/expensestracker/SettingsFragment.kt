package com.github.aptemkov.expensestracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.aptemkov.expensestracker.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    //TODO(Make secret fragment with Harry Potter quotes(faker library))
    private lateinit var binding: FragmentSettingsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        binding.textView.setOnClickListener {
            findNavController()
                .navigate(SettingsFragmentDirections
                    .actionNavigationSettingsToSecretFragment("Secret")
                )
        }
    }

}


