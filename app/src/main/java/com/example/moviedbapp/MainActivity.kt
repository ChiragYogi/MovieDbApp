package com.example.moviedbapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.moviedbapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavView = binding.bottomView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

        navController = navHostFragment.findNavController()

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_movie,R.id.navigation_tvShow ))

        bottomNavView.setupWithNavController(navController)
        setupActionBarWithNavController(navController,appBarConfiguration)

        navController.addOnDestinationChangedListener {_,destination,_ ->

            when(destination.id){

                R.id.movieDetailFragment -> hideBottomNavigationView()
                R.id.tvShowDetailsFragment -> hideBottomNavigationView()
                else -> showBottomNavigationView()
            }

        }

    }

    fun hideBottomNavigationView(){
        binding.bottomView.clearAnimation()
        binding.bottomView.animate().translationY(binding.bottomView.height.toFloat()).duration = 300
        binding.bottomView.visibility = View.GONE
    }

    fun showBottomNavigationView(){
        binding.bottomView.clearAnimation()
        binding.bottomView.animate().translationY(0f).duration = 300
        binding.bottomView.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}