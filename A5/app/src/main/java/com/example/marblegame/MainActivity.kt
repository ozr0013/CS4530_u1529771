package com.example.marblegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.marblegame.ui.MarbleGameScreen
import com.example.marblegame.ui.theme.MarbleGameTheme
import com.example.marblegame.viewmodel.MarbleViewModel

/**
 * Main Activity entry point for the Marble Game app.
 *
 * Implements MVVM architecture and handles sensor registration/unregistration
 * in onResume() and onPause() to conserve battery and resources.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: MarbleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarbleGameTheme {
                MarbleGameScreen(viewModel = viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Register sensor listener when app is in foreground
        viewModel.startListening(this)
    }

    override fun onPause() {
        super.onPause()
        // Unregister sensor listener when app goes to background to conserve resources
        viewModel.stopListening()
    }
}
