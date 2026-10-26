package com.example.courseviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courseviewer.data.CourseRepository
import com.example.courseviewer.model.Course
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for managing course data and UI state.
 *
 * Talks only to the [CourseRepository] interface (never to Room directly).
 * The course list is a StateFlow built from the repository's Flow, so the
 * UI updates automatically whenever the database changes.
 */
class CourseViewModel(
    private val repository: CourseRepository
) : ViewModel() {

    val courses: StateFlow<List<Course>> = repository.allCourses
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private val _selectedCourse = MutableStateFlow<Course?>(null)
    val selectedCourse: StateFlow<Course?> = _selectedCourse.asStateFlow()

    /**
     * Adds a new course to the database.
     */
    fun addCourse(department: String, number: String, location: String) {
        val newCourse = Course(
            department = department.trim(),
            number = number.trim(),
            location = location.trim()
        )
        viewModelScope.launch {
            repository.addCourse(newCourse)
        }
    }

    /**
     * Updates an existing course's details in the database.
     */
    fun updateCourse(id: String, department: String, number: String, location: String) {
        val updatedCourse = Course(
            id = id,
            department = department.trim(),
            number = number.trim(),
            location = location.trim()
        )
        viewModelScope.launch {
            repository.updateCourse(updatedCourse)
        }
        if (_selectedCourse.value?.id == id) {
            _selectedCourse.value = updatedCourse
        }
    }

    /**
     * Deletes a course from the database by ID.
     */
    fun deleteCourse(id: String) {
        viewModelScope.launch {
            repository.deleteCourse(id)
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
