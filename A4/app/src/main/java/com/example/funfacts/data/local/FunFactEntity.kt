package com.example.funfacts.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.funfacts.data.remote.FunFactApiDto
import java.util.UUID

/**
 * Room Database Entity for persisting fun facts locally.
 */
@Entity(tableName = "fun_facts")
data class FunFactEntity(
    @PrimaryKey val id: String,
    val text: String,
    val source: String? = null,
    val permalink: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Extension function converting an API DTO into a Room Entity.
 */
fun FunFactApiDto.toEntity(): FunFactEntity {
    return FunFactEntity(
        id = id.ifBlank { UUID.randomUUID().toString() },
        text = text.trim(),
        source = source,
        permalink = permalink,
        timestamp = System.currentTimeMillis()
    )
}
