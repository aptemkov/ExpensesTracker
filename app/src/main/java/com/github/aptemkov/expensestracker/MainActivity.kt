package com.github.aptemkov.expensestracker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.aptemkov.expensestracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }


        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_list,
                R.id.navigation_chart,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.apply {
            setupWithNavController(navController)
            background = null
            menu.getItem(2).isEnabled = false
        }


    }
    //TODO(fix launching adding fragment by pressing floating action button)
    fun openAddingFragment(view: View) {
        supportFragmentManager.commit {
            setReorderingAllowed(false)
            replace<AddItemFragment>(R.id.nav_host_fragment, args = bundleOf("item_id" to -1))
            isAddToBackStackAllowed
        }

    }

}