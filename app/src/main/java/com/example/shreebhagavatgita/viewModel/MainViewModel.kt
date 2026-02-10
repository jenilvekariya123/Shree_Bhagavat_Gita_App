package com.example.shreebhagavatgita.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shreebhagavatgita.Models.ChaptersItems
import com.example.shreebhagavatgita.Models.VerseItem
import com.example.shreebhagavatgita.dataSource.API.Room.AppDatabase
import com.example.shreebhagavatgita.dataSource.API.Room.SavedChapters
import com.example.shreebhagavatgita.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val repository: AppRepository, application: Application) : AndroidViewModel(application) {

    val savedChaptersDao = AppDatabase.getDatabaseInstance(application)!!.savedChaptersDao()

    fun getAllChapters(): Flow<List<ChaptersItems>> = repository.getAllChapters()

    fun getVerses(chapterNumber: Int): Flow<List<VerseItem>> = repository.getVerses(chapterNumber)

    fun getParticularVerse(chapterNumber: Int, verseNumber: Int): Flow<VerseItem> = repository.getParticularVerse(chapterNumber,verseNumber)

    suspend fun insertChapters(savedChapters: SavedChapters) = repository.insertChapters(savedChapters)

    fun getSavedChapters() : LiveData<List<SavedChapters>> = repository.getSavedChapters()

    fun deleteChapters(id: Int) = repository.deleteChapters(id)

    fun getParticularChapters(chapterNumber: Int) : LiveData<List<SavedChapters>> = repository.getParticularChapters(chapterNumber)

}