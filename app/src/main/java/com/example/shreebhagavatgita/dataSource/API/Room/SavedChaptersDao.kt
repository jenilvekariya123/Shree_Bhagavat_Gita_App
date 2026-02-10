package com.example.shreebhagavatgita.dataSource.API.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedChaptersDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(savedChapters: SavedChapters)

    @Query("SELECT * FROM savedChapters")
    fun getSavedChapters() : LiveData<List<SavedChapters>>

    @Query("DELETE FROM savedChapters WHERE id = :id")
    fun deleteChapters(id: Int)

    @Query("SELECT * FROM savedChapters WHERE chapterNumber = :chapter_number")
    fun getParticularChapters(chapter_number: Int) : LiveData<List<SavedChapters>>


}