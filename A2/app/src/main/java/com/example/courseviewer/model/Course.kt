package com.example.courseviewer.model

import java.util.UUID

/**
 * Data class representing a course in the application.
 *
 * @property id Unique identifier for the course.
 * @property department Department abbreviation (e.g., "CS", "MATH").
 * @property number Course number (e.g., "4530", "2250").
 * @property location Location where the course takes place (e.g., "WEB 1234", "Online").
 */
data class Course(
    val id: String = UUID.randomUUID().toString(),
    val department: String,
    val number: String,
    val location: String
) {
    /**
     * Formatted display name for the course list (e.g., "CS 4530").
     */
    val displayName: String
        get() = "$department $number".trim()
}
