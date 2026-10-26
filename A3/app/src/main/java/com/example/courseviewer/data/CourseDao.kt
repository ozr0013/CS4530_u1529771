package com.example.courseviewer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.courseviewer.model.Course
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the "courses" table.
 *
 * [getAllCourses] returns a Flow, so Room re-emits the list automatically
 * whenever a course is inserted, updated, or deleted.
 */
@Dao
interface CourseDao {

    @Query("SELECT * FROM courses ORDER BY department ASC, number ASC")
    fun getAllCourses(): Flow<List<Course>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course)

    @Update
    suspend fun updateCourse(course: Course)

    @Query("DELETE FROM courses WHERE id = :id")
    suspend fun deleteCourseById(id: String)
}
