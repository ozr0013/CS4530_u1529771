package com.example.funfacts.data.repository

import com.example.funfacts.data.local.FunFactDao
import com.example.funfacts.data.local.FunFactEntity
import com.example.funfacts.data.local.toEntity
import com.example.funfacts.data.remote.FunFactsApiService
import kotlinx.coroutines.flow.Flow

/**
 * Repository pattern: Singleton repository coordinating local Room DB and remote Ktor API.
 */
class FunFactRepository(
    private val funFactDao: FunFactDao,
    private val apiService: FunFactsApiService = FunFactsApiService
) {
    /**
     * Returns a Flow of saved facts filtered by search query.
     */
    fun getSavedFacts(searchQuery: String): Flow<List<FunFactEntity>> {
        return if (searchQuery.isBlank()) {
            funFactDao.getAllFacts()
        } else {
            funFactDao.searchFacts(searchQuery.trim())
        }
    }

    /**
     * Fetches a new random fun fact from the remote API and saves it directly to Room DB.
     */
    suspend fun fetchAndSaveRandomFact(): Result<FunFactEntity> {
        return try {
            val dto = apiService.fetchRandomFact()
            val entity = dto.toEntity()
            funFactDao.insertFact(entity)
            Result.success(entity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Deletes a fun fact from local Room DB.
     */
    suspend fun deleteFact(fact: FunFactEntity) {
        funFactDao.deleteFact(fact)
    }
}
