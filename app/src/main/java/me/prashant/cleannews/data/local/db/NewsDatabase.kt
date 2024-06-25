package me.prashant.cleannews.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import me.prashant.cleannews.data.local.model.NewsEntity

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}