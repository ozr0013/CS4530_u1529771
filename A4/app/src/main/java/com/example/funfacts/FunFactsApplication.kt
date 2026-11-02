package com.example.funfacts

import android.app.Application
import com.example.funfacts.data.local.FunFactsDatabase
import com.example.funfacts.data.repository.FunFactRepository

/**
 * Application class holding singletons (Room DB, Ktor Client, Repository).
 */
class FunFactsApplication : Application() {

    val database: FunFactsDatabase by lazy {
        FunFactsDatabase.getDatabase(this)
    }

    val repository: FunFactRepository by lazy {
        FunFactRepository(database.funFactDao())
    }
}
