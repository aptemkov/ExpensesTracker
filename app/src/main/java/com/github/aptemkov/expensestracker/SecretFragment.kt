package com.github.aptemkov.expensestracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.github.aptemkov.expensestracker.databinding.FragmentSecretBinding

class SecretFragment : Fragment() {

    private lateinit var binding: FragmentSecretBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSecretBinding.bind(view)
        clearToolbarMenu()
        binding.myToolbar.inflateMenu(R.menu.secret_fragment_menu)
        binding.myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.secret_done -> {
                    Toast.makeText(activity?.applicationContext, "Done", Toast.LENGTH_SHORT ).show()
                    true
                }

                else -> false
            }

        }
    }


    // before replacing menu if is necessary
    fun clearToolbarMenu() {
        binding.myToolbar.menu.clear()
    }
}