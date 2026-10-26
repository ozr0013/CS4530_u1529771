package com.example.courseviewer.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.courseviewer.data.CourseDatabase
import com.example.courseviewer.data.CourseRepository
import com.example.courseviewer.data.RoomCourseRepository

/**
 * Factory that creates [CourseViewModel] with its repository dependency.
 *
 * Enforces the singleton pattern: [fromContext] always hands the ViewModel
 * the single [RoomCourseRepository] instance, so every ViewModel created
 * through this factory shares the same repository.
 */
class CourseViewModelFactory(
    private val repository: CourseRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            return CourseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        /**
         * Builds a factory backed by the app-wide singleton repository.
         */
        fun fromContext(context: Context): CourseViewModelFactory {
            val dao = CourseDatabase.getDatabase(context).courseDao()
            return CourseViewModelFactory(RoomCourseRepository.getInstance(dao))
        }
    }
}
