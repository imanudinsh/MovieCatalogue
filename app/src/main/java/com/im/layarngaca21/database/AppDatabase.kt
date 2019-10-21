package com.im.layarngaca21.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.im.layarngaca21.database.dao.FavoriteDao
import com.im.layarngaca21.database.entity.Favorite


@Database(entities = [
    Favorite::class
], version = 2, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "layarNgaca21DB")
                                .fallbackToDestructiveMigration()
                                .build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}