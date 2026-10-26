package com.example.courseviewer

import com.example.courseviewer.data.CourseRepository
import com.example.courseviewer.model.Course
import com.example.courseviewer.viewmodel.CourseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * In-memory stand-in for the Room repository, possible because the
 * ViewModel depends only on the [CourseRepository] interface.
 */
private class FakeCourseRepository : CourseRepository {
    private val store = MutableStateFlow<List<Course>>(emptyList())
    override val allCourses: Flow<List<Course>> = store

    override suspend fun addCourse(course: Course) = store.update { it + course }

    override suspend fun updateCourse(course: Course) = store.update { list ->
        list.map { if (it.id == course.id) course else it }
    }

    override suspend fun deleteCourse(id: String) = store.update { list ->
        list.filterNot { it.id == id }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class CourseViewModelTest {

    private lateinit var repository: FakeCourseRepository
    private lateinit var viewModel: CourseViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = FakeCourseRepository()
        viewModel = CourseViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialCourses_areEmpty() {
        assertTrue(viewModel.courses.value.isEmpty())
    }

    @Test
    fun addCourse_appearsInList() {
        viewModel.addCourse(department = " CS ", number = "4530", location = "WEB L104")

        val courses = viewModel.courses.value
        assertEquals(1, courses.size)
        assertEquals("CS", courses.first().department)
        assertEquals("4530", courses.first().number)
        assertEquals("WEB L104", courses.first().location)
    }

    @Test
    fun deleteCourse_removesFromListAndClearsSelection() {
        viewModel.addCourse("CS", "4530", "WEB L104")
        val course = viewModel.courses.value.first()
        viewModel.selectCourse(course)

        viewModel.deleteCourse(course.id)

        assertTrue(viewModel.courses.value.isEmpty())
        assertNull(viewModel.selectedCourse.value)
    }

    @Test
    fun updateCourse_changesStoredCourse() {
        viewModel.addCourse("CS", "4530", "WEB L104")
        val course = viewModel.courses.value.first()

        viewModel.updateCourse(course.id, "CS", "4540", "MEB 2180")

        val updated = viewModel.courses.value.single()
        assertEquals(course.id, updated.id)
        assertEquals("4540", updated.number)
        assertEquals("MEB 2180", updated.location)
    }
}
