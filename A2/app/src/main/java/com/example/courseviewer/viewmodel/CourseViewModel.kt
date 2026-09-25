package com.example.courseviewer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.courseviewer.model.Course
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for managing course data and UI state.
 *
 * Follows MVVM architecture: stores, updates, and exposes course list
 * and selection state to the UI. State survives recompositions and
 * configuration changes.
 */
class CourseViewModel : ViewModel() {

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses.asStateFlow()

    private val _selectedCourse = MutableStateFlow<Course?>(null)
    val selectedCourse: StateFlow<Course?> = _selectedCourse.asStateFlow()

    init {
        loadInitialCourses()
    }

    /**
     * Pre-loads default courses for demonstration.
     */
    private fun loadInitialCourses() {
        _courses.value = listOf(
            Course(department = "CS", number = "4530", location = "WEB L104"),
            Course(department = "CS", number = "3500", location = "MEB 2180"),
            Course(department = "MATH", number = "2250", location = "JWB 335"),
            Course(department = "ECE", number = "1400", location = "WEB 2230"),
            Course(department = "PHYS", number = "2210", location = "JFB 101")
        )
    }

    /**
     * Adds a new course to the list.
     */
    fun addCourse(department: String, number: String, location: String) {
        val newCourse = Course(
            department = department.trim(),
            number = number.trim(),
            location = location.trim()
        )
        _courses.update { currentList ->
            currentList + newCourse
        }
    }

    /**
     * Updates an existing course's details.
     */
    fun updateCourse(id: String, department: String, number: String, location: String) {
        val updatedCourse = Course(
            id = id,
            department = department.trim(),
            number = number.trim(),
            location = location.trim()
        )
        _courses.update { currentList ->
            currentList.map { course ->
                if (course.id == id) updatedCourse else course
            }
        }
        if (_selectedCourse.value?.id == id) {
            _selectedCourse.value = updatedCourse
        }
    }

    /**
     * Deletes a course from the list by ID.
     */
    fun deleteCourse(id: String) {
        _courses.update { currentList ->
            currentList.filterNot { it.id == id }
        }
        if (_selectedCourse.value?.id == id) {
            _selectedCourse.value = null
        }
    }

    /**
     * Sets the currently selected course for detailed view.
     */
    fun selectCourse(course: Course?) {
        _selectedCourse.value = course
    }
}
