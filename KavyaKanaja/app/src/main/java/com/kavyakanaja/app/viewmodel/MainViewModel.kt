package com.kavyakanaja.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kavyakanaja.app.data.model.Poem
import com.kavyakanaja.app.data.model.Poet
import com.kavyakanaja.app.data.repository.PoemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PoemRepository(application)

    private val _poemOfTheDay = MutableStateFlow<Poem?>(null)
    val poemOfTheDay: StateFlow<Poem?> = _poemOfTheDay.asStateFlow()

    private val _allPoems = MutableStateFlow<List<Poem>>(emptyList())
    val allPoems: StateFlow<List<Poem>> = _allPoems.asStateFlow()

    private val _poets = MutableStateFlow<List<Poet>>(emptyList())
    val poets: StateFlow<List<Poet>> = _poets.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Poem>>(emptyList())
    val searchResults: StateFlow<List<Poem>> = _searchResults.asStateFlow()

    private val _selectedPoem = MutableStateFlow<Poem?>(null)
    val selectedPoem: StateFlow<Poem?> = _selectedPoem.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _poemOfTheDay.value = repository.getPoemOfTheDay()
        _allPoems.value = repository.getPoems()
        _poets.value = repository.getPoets()
    }

    fun selectPoem(poem: Poem) {
        _selectedPoem.value = poem
    }

    fun search(query: String) {
        _searchQuery.value = query
        _searchResults.value = if (query.isBlank()) {
            repository.getPoems()
        } else {
            repository.searchPoems(query)
        }
    }

    fun getCategories(): List<String> = repository.getCategories()

    fun getPoemsByCategory(category: String): List<Poem> =
        repository.getPoemsByCategory(category)
}
