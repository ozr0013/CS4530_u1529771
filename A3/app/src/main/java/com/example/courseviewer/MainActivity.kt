package com.example.courseviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.courseviewer.ui.CourseScreen
import com.example.courseviewer.ui.theme.CourseViewerTheme
import com.example.courseviewer.viewmodel.CourseViewModel
import com.example.courseviewer.viewmodel.CourseViewModelFactory

/**
 * Main Single Activity for the Course Viewer & Editor application.
 *
 * The ViewModel is created through [CourseViewModelFactory], which supplies
 * the singleton Room-backed repository.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: CourseViewModel by viewModels {
        CourseViewModelFactory.fromContext(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CourseViewerTheme {
                CourseScreen(viewModel = viewModel)
            }
        }
    }
}
