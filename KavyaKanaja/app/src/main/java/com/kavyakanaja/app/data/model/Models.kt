package com.kavyakanaja.app.data.model

data class Poem(
    val id: Int,
    val title: String,
    val titleKannada: String,
    val poet: String,
    val poetKannada: String,
    val verse: String,
    val verseKannada: String,
    val bhavartha: String,
    val bhavarthaKannada: String,
    val era: String,
    val category: String,
    val words: List<WordMeaning> = emptyList()
)

data class WordMeaning(
    val word: String,
    val meaning: String,
    val meaningKannada: String
)

data class Poet(
    val id: Int,
    val name: String,
    val nameKannada: String,
    val born: String,
    val died: String,
    val award: String,
    val bio: String,
    val bioKannada: String,
    val famousWorks: List<String>
)
