package com.example.courseviewer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.courseviewer.model.Course
import com.example.courseviewer.ui.components.CourseAddEditDialog
import com.example.courseviewer.ui.components.CourseDetailDialog
import com.example.courseviewer.ui.components.CourseItem
import com.example.courseviewer.viewmodel.CourseViewModel

/**
 * Main screen for viewing, adding, editing, and deleting courses.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseScreen(
    viewModel: CourseViewModel,
    modifier: Modifier = Modifier
) {
    val courses by viewModel.courses.collectAsState()
    val selectedCourse by viewModel.selectedCourse.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var courseToEdit by remember { mutableStateOf<Course?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Course Viewer",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    courseToEdit = null
                    showAddDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Course"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (courses.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No courses added yet.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap the + button to add a course.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = courses,
                        key = { it.id }
                    ) { course ->
                        CourseItem(
                            course = course,
                            onClick = {
                                viewModel.selectCourse(course)
                            },
                            onEdit = {
                                courseToEdit = course
                                showAddDialog = true
                            },
                            onDelete = {
                                viewModel.deleteCourse(course.id)
                            }
                        )
                    }
                }
            }
        }
    }

    // Course Detail Dialog
    selectedCourse?.let { course ->
        CourseDetailDialog(
            course = course,
            onDismiss = {
                viewModel.selectCourse(null)
            },
            onEdit = { targetCourse ->
                courseToEdit = targetCourse
                showAddDialog = true
            },
            onDelete = { targetCourse ->
                viewModel.deleteCourse(targetCourse.id)
            }
        )
    }

    // Add / Edit Course Dialog
    if (showAddDialog) {
        CourseAddEditDialog(
            courseToEdit = courseToEdit,
            onDismiss = {
                showAddDialog = false
                courseToEdit = null
            },
            onSave = { department, number, location ->
                val currentEditingCourse = courseToEdit
                if (currentEditingCourse != null) {
                    viewModel.updateCourse(
                        id = currentEditingCourse.id,
                        department = department,
                        number = number,
                        location = location
                    )
                } else {
                    viewModel.addCourse(
                        department = department,
                        number = number,
                        location = location
                    )
                }
                showAddDialog = false
                courseToEdit = null
            }
        )
    }
}
