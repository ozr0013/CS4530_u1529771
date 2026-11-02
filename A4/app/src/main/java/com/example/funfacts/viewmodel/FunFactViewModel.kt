package com.example.funfacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.funfacts.data.local.FunFactEntity
import com.example.funfacts.data.repository.FunFactRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for managing fun facts state and search filtering.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FunFactViewModel(
    private val repository: FunFactRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isFetching = MutableStateFlow(false)
    val isFetching: StateFlow<Boolean> = _isFetching.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    /**
     * Reactively streams fun facts from Room DB based on the current search query.
     */
    val facts: StateFlow<List<FunFactEntity>> = _searchQuery
        .flatMapLatest { query ->
            repository.getSavedFacts(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Updates search query for filtering local fun facts.
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    /**
     * Fetches a new fun fact from remote API and persists it to local Room DB.
     */
    fun fetchNewFact() {
        viewModelScope.launch {
            _isFetching.value = true
            val result = repository.fetchAndSaveRandomFact()
            result.fold(
                onSuccess = {
                    _userMessage.value = "New fun fact added!"
                },
                onFailure = { error ->
                    _userMessage.value = "Failed to fetch fact: ${error.localizedMessage ?: "Network error"}"
                }
            )
            _isFetching.value = false
        }
    }

    /**
     * Deletes a fun fact from local Room DB.
     */
    fun deleteFact(fact: FunFactEntity) {
        viewModelScope.launch {
            repository.deleteFact(fact)
            _userMessage.value = "Fact deleted."
        }
    }

    /**
     * Clears snackbar user message after display.
     */
    fun clearUserMessage() {
        _userMessage.value = null
    }
}
