package com.vikas.guhyagyan.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vikas.guhyagyan.R
import com.vikas.guhyagyan.databinding.ActivityFirstMainBinding
import com.vikas.guhyagyan.fragments.*

class FirstMainActivity : AppCompatActivity() {

    private var currentFragmentTag = "home"

    private lateinit var binding: ActivityFirstMainBinding
    private lateinit var activeFragment: Fragment

    private val homeFragment = HomeFragment()
    private val liveFragment = LiveFragment()
    private val tasksFragment = TasksFragment()
    private val leaderboardFragment = LeaderboardFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFirstMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment(), "home")
        // Bottom navigation handling
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val fragmentTag = when (item.itemId) {
                R.id.nav_home -> "home"
                R.id.liveFragment -> "live"
                R.id.nav_tasks -> "tasks"
                R.id.nav_leaderboard -> "leaderboard"
                R.id.nav_profile -> "profile"
                else -> "home"
            }

            val fragment = when (fragmentTag) {
                "home" -> HomeFragment()
                "live" -> LiveFragment()
                "tasks" -> TasksFragment()
                "leaderboard" -> LeaderboardFragment()
                "profile" -> ProfileFragment()
                else -> HomeFragment()
            }
            replaceFragment(fragment, fragmentTag)
            true
        }
            // Back press logic
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (currentFragmentTag != "home") {
                        // Go to home first
                        replaceFragment(HomeFragment(), "home")
                        binding.bottomNavigationView.selectedItemId = R.id.nav_home
                    } else {
                        // Default behavior - exit app
                        finish()
                    }
                }
            })
        }

        private fun replaceFragment(fragment: Fragment, tag: String) {
            currentFragmentTag = tag
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, fragment)
                .commit()
        }
    }

