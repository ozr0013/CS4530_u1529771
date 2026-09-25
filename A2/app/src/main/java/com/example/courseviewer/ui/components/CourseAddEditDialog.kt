package com.example.courseviewer.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.courseviewer.model.Course

/**
 * Dialog for adding a new course or editing an existing course.
 *
 * Pre-populates fields if editing. Disables save button if any input field is blank.
 */
@Composable
fun CourseAddEditDialog(
    courseToEdit: Course? = null,
    onDismiss: () -> Unit,
    onSave: (department: String, number: String, location: String) -> Unit
) {
    var department by remember { mutableStateOf(courseToEdit?.department ?: "") }
    var number by remember { mutableStateOf(courseToEdit?.number ?: "") }
    var location by remember { mutableStateOf(courseToEdit?.location ?: "") }

    val isFormValid = department.isNotBlank() && number.isNotBlank() && location.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (courseToEdit == null) "Add New Course" else "Edit Course",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = department,
                    onValueChange = { department = it },
                    label = { Text("Department (e.g., CS)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text("Course Number (e.g., 4530)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location (e.g., WEB L104)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isFormValid) {
                        onSave(department, number, location)
                    }
                },
                enabled = isFormValid
            ) {
                Text(if (courseToEdit == null) "Add Course" else "Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
