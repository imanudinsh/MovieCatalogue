package com.im.layarngaca21.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.im.layarngaca21.database.entity.Favorite

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("SELECT * FROM Favorite WHERE id = :id and category = :category")
    fun getById(id: String, category: String): LiveData<List<Favorite>>

    @Query("SELECT * FROM Favorite WHERE category = :category")
    fun getByCategory(category: String): LiveData<List<Favorite>>

}