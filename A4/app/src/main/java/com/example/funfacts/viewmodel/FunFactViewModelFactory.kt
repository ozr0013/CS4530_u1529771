package com.example.funfacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.funfacts.data.repository.FunFactRepository

/**
 * Factory for creating FunFactViewModel with constructor parameters.
 */
class FunFactViewModelFactory(
    private val repository: FunFactRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FunFactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FunFactViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
