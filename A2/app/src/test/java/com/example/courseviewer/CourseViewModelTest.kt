package com.example.courseviewer

import com.example.courseviewer.viewmodel.CourseViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CourseViewModelTest {

    private lateinit var viewModel: CourseViewModel

    @Before
    fun setUp() {
        viewModel = CourseViewModel()
    }

    @Test
    fun initialCourses_areLoaded() {
        val courses = viewModel.courses.value
        assertTrue(courses.isNotEmpty())
        assertEquals(5, courses.size)
    }

    @Test
    fun addCourse_increasesListSize() {
        val initialSize = viewModel.courses.value.size
        viewModel.addCourse(
            department = "CS",
            number = "4500",
            location = "WEB 1234"
        )
        val updatedCourses = viewModel.courses.value
        assertEquals(initialSize + 1, updatedCourses.size)

        val addedCourse = updatedCourses.last()
        assertEquals("CS", addedCourse.department)
        assertEquals("4500", addedCourse.number)
        assertEquals("WEB 1234", addedCourse.location)
        assertEquals("CS 4500", addedCourse.displayName)
    }

    @Test
    fun updateCourse_modifiesCourseDetails() {
        val courseToUpdate = viewModel.courses.value.first()
        viewModel.updateCourse(
            id = courseToUpdate.id,
            department = "CS",
            number = "9999",
            location = "New Room"
        )

        val updatedCourse = viewModel.courses.value.find { it.id == courseToUpdate.id }
        assertNotNull(updatedCourse)
        assertEquals("CS", updatedCourse?.department)
        assertEquals("9999", updatedCourse?.number)
        assertEquals("New Room", updatedCourse?.location)
    }

    @Test
    fun deleteCourse_removesCourseFromList() {
        val courseToDelete = viewModel.courses.value.first()
        val initialSize = viewModel.courses.value.size

        viewModel.deleteCourse(courseToDelete.id)

        val updatedCourses = viewModel.courses.value
        assertEquals(initialSize - 1, updatedCourses.size)
        assertNull(updatedCourses.find { it.id == courseToDelete.id })
    }

    @Test
    fun selectCourse_updatesSelectedState() {
        val course = viewModel.courses.value.first()
        viewModel.selectCourse(course)

        assertEquals(course, viewModel.selectedCourse.value)

        viewModel.selectCourse(null)
        assertNull(viewModel.selectedCourse.value)
    }
}
