package com.example.shreebhagavatgita.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.shreebhagavatgita.Models.ChaptersItems
import com.example.shreebhagavatgita.Models.VerseItem
import com.example.shreebhagavatgita.dataSource.API.ApiInterface
import com.example.shreebhagavatgita.dataSource.API.ApiUtilities
import com.example.shreebhagavatgita.dataSource.API.Room.SavedChapters
import com.example.shreebhagavatgita.dataSource.API.Room.SavedChaptersDao
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository(private var apiInterface: ApiInterface, private var savedChaptersDao: SavedChaptersDao) {

    fun getAllChapters(): Flow<List<ChaptersItems>> = callbackFlow{
        val call = apiInterface.getAllChapter()

        val callBack = object : Callback<List<ChaptersItems>> {
            override fun onResponse(
                call: Call<List<ChaptersItems>>,
                response: Response<List<ChaptersItems>>
            ) {
                if (response.isSuccessful && response.body() != null){
                    trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(call: Call<List<ChaptersItems>>, t: Throwable) {
                close(t)
            }
        }
        call.enqueue(callBack)

        awaitClose {
            call.cancel()
        }

    }

    fun getVerses(chapterNumber: Int): Flow<List<VerseItem>> = callbackFlow {
        val call = apiInterface.getAllVerse(chapterNumber)
        val callBack = object : Callback<List<VerseItem>> {
            override fun onResponse(
                call: Call<List<VerseItem>>,
                response: Response<List<VerseItem>>
            ) {
                if (response.isSuccessful && response.body() != null){
                    trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(call: Call<List<VerseItem>>, t: Throwable) {
                close(t)
            }
        }
        call.enqueue(callBack)

        awaitClose {
            call.cancel()
        }
    }

    fun getParticularVerse(chapterNumber: Int, verseNumber: Int): Flow<VerseItem> = callbackFlow {
        val call = apiInterface.getParticularVerse(chapterNumber, verseNumber)
        val callBack = object : Callback<VerseItem> {
            override fun onResponse(
                call: Call<VerseItem>,
                response: Response<VerseItem>
            ) {
                if (response.isSuccessful && response.body() != null){
                    trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(call: Call<VerseItem>, t: Throwable) {
                close(t)
            }
        }
        call.enqueue(callBack)

        awaitClose {
            call.cancel()
        }
    }

    suspend fun insertChapters(savedChapters: SavedChapters) = savedChaptersDao.insertChapters(savedChapters)

    fun getSavedChapters() : LiveData<List<SavedChapters>> = savedChaptersDao.getSavedChapters()
    fun deleteChapters(id: Int) = savedChaptersDao.deleteChapters(id)
    fun getParticularChapters(chapterNumber: Int) : LiveData<List<SavedChapters>> = savedChaptersDao.getParticularChapters(chapterNumber)


}