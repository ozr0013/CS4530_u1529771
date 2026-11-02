package com.example.funfacts.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room Database singleton class.
 */
@Database(entities = [FunFactEntity::class], version = 1, exportSchema = false)
abstract class FunFactsDatabase : RoomDatabase() {

    abstract fun funFactDao(): FunFactDao

    companion object {
        @Volatile
        private var INSTANCE: FunFactsDatabase? = null

        fun getDatabase(context: Context): FunFactsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FunFactsDatabase::class.java,
                    "fun_facts_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
