package com.example.shreebhagavatgita.dataSource.API.Room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "savedChapters")
data class SavedChapters(

    @PrimaryKey
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    val nameTransliterated: String? = null,
    val nameTranslated: String? = null,
    val versesCount: Int? = null,
    val chapterNumber: Int? = null,
    val nameMeaning: String? = null,
    val chapterSummary: String? = null,
    val chapterSummaryHindi: String? = null,
    val verses: List<String>
)