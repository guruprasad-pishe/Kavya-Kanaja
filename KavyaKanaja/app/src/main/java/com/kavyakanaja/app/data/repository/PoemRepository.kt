package com.kavyakanaja.app.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.kavyakanaja.app.data.model.Poem
import com.kavyakanaja.app.data.model.Poet
import java.time.LocalDate

data class PoemsData(
    @SerializedName("poems") val poems: List<Poem>
)

data class PoetsData(
    @SerializedName("poets") val poets: List<Poet>
)

class PoemRepository(private val context: Context) {

    private val gson = Gson()
    private var _poems: List<Poem>? = null
    private var _poets: List<Poet>? = null

    fun getPoems(): List<Poem> {
        if (_poems == null) {
            val json = context.assets.open("poems.json").bufferedReader().use { it.readText() }
            _poems = gson.fromJson(json, PoemsData::class.java).poems
        }
        return _poems ?: emptyList()
    }

    fun getPoets(): List<Poet> {
        if (_poets == null) {
            val json = context.assets.open("poets.json").bufferedReader().use { it.readText() }
            _poets = gson.fromJson(json, PoetsData::class.java).poets
        }
        return _poets ?: emptyList()
    }

    fun getPoemOfTheDay(): Poem {
        val poems = getPoems()
        val dayOfYear = LocalDate.now().dayOfYear
        val index = dayOfYear % poems.size
        return poems[index]
    }

    fun getPoemById(id: Int): Poem? {
        return getPoems().find { it.id == id }
    }

    fun getPoemsByCategory(category: String): List<Poem> {
        return getPoems().filter { it.category.equals(category, ignoreCase = true) }
    }

    fun getCategories(): List<String> {
        return getPoems().map { it.category }.distinct()
    }

    fun searchPoems(query: String): List<Poem> {
        val q = query.lowercase()
        return getPoems().filter {
            it.title.lowercase().contains(q) ||
            it.poet.lowercase().contains(q) ||
            it.verseKannada.contains(q) ||
            it.titleKannada.contains(q) ||
            it.poetKannada.contains(q)
        }
    }
}
