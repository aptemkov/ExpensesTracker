package com.github.aptemkov.expensestracker

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.aptemkov.expensestracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        //supportActionBar?.hide()

        navController = findNavController(R.id.nav_host_fragment)
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
            /* TODO(Floating action button in bottom nav bar)
            menu.getItem(2).isEnabled = false
            */
        }

            navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.addItemFragment -> hideBottomNav(View.GONE)
                R.id.itemDetailFragment -> hideBottomNav(View.INVISIBLE)
                else -> showBottomNav()
            }
        }
    }


    private fun showBottomNav() {
        var a = View.VISIBLE
        binding.bottomAppBar.visibility = View.VISIBLE
        //supportActionBar?.hide()
    }

    private fun hideBottomNav(visibility: Int) {
        binding.bottomAppBar.visibility = visibility
        //supportActionBar?.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    //TODO(fix launching adding fragment by pressing floating action button)
    fun openAddingFragment(view: View) {
        /*supportFragmentManager
            .beginTransaction()
            .replace<AddItemFragment>(R.id.nav_host_fragment, args = bundleOf("item_id" to -1))
            .addToBackStack(null)
            .commit()*/
        Toast.makeText(this, "SOON", Toast.LENGTH_SHORT).show()
    }


}