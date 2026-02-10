package com.example.shreebhagavatgita.dataSource.API.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [SavedChapters::class], version = 1, exportSchema = false)
@androidx.room.TypeConverters(TypeConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun savedChaptersDao(): SavedChaptersDao

   companion object{
       @Volatile
       var INSTANCE: AppDatabase? = null

       fun getDatabaseInstance(context: Context): AppDatabase?{
           val tempInstance = INSTANCE

           if (INSTANCE != null) return tempInstance
           synchronized(this){
               val roomDB = Room.databaseBuilder(
                   context.applicationContext,
                   AppDatabase::class.java,
                   "savedChapters").fallbackToDestructiveMigration().build()

               INSTANCE = roomDB
               return INSTANCE
           }

       }
   }
}