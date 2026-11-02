package com.example.funfacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.funfacts.ui.FunFactsScreen
import com.example.funfacts.ui.theme.FunFactsTheme
import com.example.funfacts.viewmodel.FunFactViewModel
import com.example.funfacts.viewmodel.FunFactViewModelFactory

/**
 * Main Activity entry point for Fun Facts application.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: FunFactViewModel by viewModels {
        val app = application as FunFactsApplication
        FunFactViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FunFactsTheme {
                FunFactsScreen(viewModel = viewModel)
            }
        }
    }
}
