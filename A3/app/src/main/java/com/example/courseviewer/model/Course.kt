package com.example.courseviewer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Room entity representing a course stored in the "courses" table.
 *
 * @property id Unique identifier for the course (primary key).
 * @property department Department abbreviation (e.g., "CS", "MATH").
 * @property number Course number (e.g., "4530", "2250").
 * @property location Location where the course takes place (e.g., "WEB 1234", "Online").
 */
@Entity(tableName = "courses")
data class Course(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "department")
    val department: String,
    @ColumnInfo(name = "number")
    val number: String,
    @ColumnInfo(name = "location")
    val location: String
)

/**
 * Formatted display name for the course list (e.g., "CS 4530").
 * Declared as an extension so Room does not try to map it to a column.
 */
val Course.displayName: String
    get() = "$department $number".trim()
