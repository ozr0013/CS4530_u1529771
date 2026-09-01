package com.example.emailsplitter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.emailsplitter.databinding.ActivityMainBinding

/**
 * Single Activity entry point. It owns no UI of its own beyond the container,
 * it just hosts whichever Fragment should be on screen.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Only add the first Fragment on a genuinely fresh start. On a rotation
        // the FragmentManager restores whatever was already there, so adding
        // again would stack a duplicate on top.
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fl_frag_container, InputFragment(), InputFragment.TAG)
                .commit()
        }
    }
}
