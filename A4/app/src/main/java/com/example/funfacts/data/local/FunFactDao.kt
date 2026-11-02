package com.example.funfacts.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for local Room database operations.
 */
@Dao
interface FunFactDao {

    @Query("SELECT * FROM fun_facts ORDER BY timestamp DESC")
    fun getAllFacts(): Flow<List<FunFactEntity>>

    @Query("SELECT * FROM fun_facts WHERE text LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchFacts(query: String): Flow<List<FunFactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFact(fact: FunFactEntity)

    @Delete
    suspend fun deleteFact(fact: FunFactEntity)
}
