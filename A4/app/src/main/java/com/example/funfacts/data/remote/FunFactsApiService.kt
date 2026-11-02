package com.example.funfacts.data.remote

import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * Singleton service for fetching random fun facts from uselessfacts.jsph.pl API.
 */
object FunFactsApiService {
    private const val RANDOM_FACT_URL = "https://uselessfacts.jsph.pl/random.json?language=en"

    suspend fun fetchRandomFact(): FunFactApiDto {
        return KtorClient.httpClient.get(RANDOM_FACT_URL).body()
    }
}
