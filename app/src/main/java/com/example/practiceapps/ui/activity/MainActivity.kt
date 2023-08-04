package com.example.practiceapps.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.practiceapps.R
import com.example.practiceapps.databinding.ActivityMainBinding
import com.example.practiceapps.ui.fragment.PhotoDetailsListFragment
import com.example.practiceapps.ui.fragment.UserRecordsFragment
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavDrawer()
        setDefaultFragment()
        customOnBackPressDispatcher()
    }

    /**
     * This function will set navigation drawer in the traditional way. Navigation Component
     * implementation is in progress.
     * */
    private fun setUpNavDrawer() {
        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(
                this,
                binding.myDrawerLayout,
                R.string.nav_open,
                R.string.nav_close
            )
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
        binding.navigationView.setNavigationItemSelectedListener(this)
        actionBarDrawerToggle.syncState()
    }

    /**
     * This function will set the default fragment (in current case HomeFragment) on app startup
     * on the MainActivity.
     * */
    private fun setDefaultFragment() {
        supportFragmentManager.beginTransaction().addToBackStack(null)
            .add(
                binding.fragmentContainer.id,
                PhotoDetailsListFragment.newInstance(),
                PhotoDetailsListFragment::class.java.simpleName
            )
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (binding.myDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                binding.myDrawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.myDrawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.userList -> {
                supportFragmentManager.beginTransaction().addToBackStack(null)
                    .add(
                        binding.fragmentContainer.id,
                        PhotoDetailsListFragment.newInstance(),
                        PhotoDetailsListFragment::class.java.simpleName
                    )
                    .commit()
            }

            R.id.publicApi -> {
                supportFragmentManager.beginTransaction().addToBackStack(null)
                    .add(
                        binding.fragmentContainer.id,
                        UserRecordsFragment.newInstance(),
                        UserRecordsFragment::class.java.simpleName
                    )
                    .commit()
            }
        }
        binding.myDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * This function uses the OnBackPressDispatcher which is the replacement of OnBackPressed
     * in Androidx.
     * */
    private fun customOnBackPressDispatcher() {
        onBackPressedDispatcher.addCallback(
            this@MainActivity,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (supportFragmentManager.backStackEntryCount == 1) {
                        finish()
                    } else {
                        supportFragmentManager.popBackStack()
                    }
                }
            })
    }

    //===============NavComponent
    /*private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment*/
    /*private fun setUpNavHost() {
        setSupportActionBar(binding.appBarMain.toolbar)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home_fragment),
            binding.drawerLayout
        )
        binding.navView.itemIconTintList = null
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                    as NavHostFragment
        navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_apiEntries -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    binding.appBarMain.toolbar.setNavigationOnClickListener {
                        navController.navigateUp()
                    }
                }

                else -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    binding.appBarMain.toolbar.setNavigationOnClickListener {
                        binding.drawerLayout.openDrawer(GravityCompat.START)
                    }
                }
            }
        }
    }*/

    /**
     * Function to display the back navigation icon and home icon
     */
    /*fun showBackIcon() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.appBarMain.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
    }*/

    /*override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController, appBarConfiguration
        ) || super.onSupportNavigateUp()
    }*/

}