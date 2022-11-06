package com.example.newsapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.models.Article


@Database(
    entities = [Article::class],
    version = 2,
)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun getDao(): NewsDAO

    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null
        private val LOCK = Any()

        fun createDatabase(context: Context): NewsDatabase {
            if (INSTANCE != null) return INSTANCE!!
            else {
                synchronized(LOCK) {
                    val newInst = Room.databaseBuilder(
                        context.applicationContext,
                        NewsDatabase::class.java,
                        "articles_db"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = newInst
                    return newInst
                }
            }
        }


    }
}