package com.example.courseviewer.data

import com.example.courseviewer.model.Course
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface: the layer between the ViewModel and storage.
 *
 * The ViewModel only depends on this interface, so it never talks to
 * Room directly and the storage can be swapped (e.g., a fake in tests).
 */
interface CourseRepository {
    val allCourses: Flow<List<Course>>
    suspend fun addCourse(course: Course)
    suspend fun updateCourse(course: Course)
    suspend fun deleteCourse(id: String)
}

/**
 * Room-backed implementation of [CourseRepository].
 *
 * Singleton: the constructor is private and the only way to get an
 * instance is [getInstance], so exactly one repository exists in the app.
 */
class RoomCourseRepository private constructor(
    private val courseDao: CourseDao
) : CourseRepository {

    override val allCourses: Flow<List<Course>> = courseDao.getAllCourses()

    override suspend fun addCourse(course: Course) = courseDao.insertCourse(course)

    override suspend fun updateCourse(course: Course) = courseDao.updateCourse(course)

    override suspend fun deleteCourse(id: String) = courseDao.deleteCourseById(id)

    companion object {
        @Volatile
        private var INSTANCE: RoomCourseRepository? = null

        fun getInstance(courseDao: CourseDao): RoomCourseRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: RoomCourseRepository(courseDao).also { INSTANCE = it }
            }
        }
    }
}
