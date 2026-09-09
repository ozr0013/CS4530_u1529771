package com.example.helloandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.helloandroid.databinding.ActivityMainBinding

/**
 * Single entry point for the app. Hosts every fragment inside one container
 * and is responsible only for showing the initial screen.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Only add the menu fragment on a fresh launch. After a rotation the
        // fragment manager restores it automatically, and adding it again
        // would stack a duplicate copy on top of the existing one.
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ButtonMenuFragment())
                .commit()
        }
    }
}