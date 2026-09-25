package com.example.courseviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.courseviewer.ui.CourseScreen
import com.example.courseviewer.ui.theme.CourseViewerTheme
import com.example.courseviewer.viewmodel.CourseViewModel

/**
 * Main Single Activity for the Course Viewer & Editor application.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: CourseViewModel by viewModels()

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
