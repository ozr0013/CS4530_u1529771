package com.example.funfacts

import com.example.funfacts.data.local.FunFactDao
import com.example.funfacts.data.local.FunFactEntity
import com.example.funfacts.data.remote.FunFactApiDto
import com.example.funfacts.data.remote.FunFactsApiService
import com.example.funfacts.data.repository.FunFactRepository
import com.example.funfacts.viewmodel.FunFactViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FunFactViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeDao: FakeFunFactDao
    private lateinit var fakeApiService: FakeFunFactsApiService
    private lateinit var repository: FunFactRepository
    private lateinit var viewModel: FunFactViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeFunFactDao()
        fakeApiService = FakeFunFactsApiService()
        repository = FunFactRepository(fakeDao, fakeApiService)
        viewModel = FunFactViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun searchQueryChange_updatesQuery() {
        viewModel.onSearchQueryChange("space")
        assertEquals("space", viewModel.searchQuery.value)
    }

    @Test
    fun fetchNewFact_savesToRepository() = runTest {
        viewModel.fetchNewFact()
        testDispatcher.scheduler.advanceUntilIdle()

        val savedFacts = fakeDao.getAllFacts().first()
        assertEquals(1, savedFacts.size)
        assertEquals("The moon moves 3.8 cm away from Earth each year.", savedFacts.first().text)
    }

    @Test
    fun deleteFact_removesFromRepository() = runTest {
        val fact = FunFactEntity("1", "Sample Fact")
        fakeDao.insertFact(fact)

        viewModel.deleteFact(fact)
        testDispatcher.scheduler.advanceUntilIdle()

        val savedFacts = fakeDao.getAllFacts().first()
        assertEquals(0, savedFacts.size)
    }
}

class FakeFunFactDao : FunFactDao {
    private val db = mutableListOf<FunFactEntity>()
    private val flow = MutableStateFlow<List<FunFactEntity>>(emptyList())

    override fun getAllFacts(): Flow<List<FunFactEntity>> {
        flow.value = db.toList()
        return flow
    }

    override fun searchFacts(query: String): Flow<List<FunFactEntity>> {
        val filtered = db.filter { it.text.contains(query, ignoreCase = true) }
        return MutableStateFlow(filtered)
    }

    override suspend fun insertFact(fact: FunFactEntity) {
        db.add(0, fact)
        flow.value = db.toList()
    }

    override suspend fun deleteFact(fact: FunFactEntity) {
        db.removeAll { it.id == fact.id }
        flow.value = db.toList()
    }
}

class FakeFunFactsApiService : FunFactsApiService {
    override suspend fun fetchRandomFact(): FunFactApiDto {
        return FunFactApiDto(
            id = "fact-123",
            text = "The moon moves 3.8 cm away from Earth each year.",
            source = "NASA"
        )
    }
}
