package com.im.layarngaca21.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import android.database.Cursor
import com.im.layarngaca21.database.entity.Favorite

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: Favorite): Long

    @Delete
    fun delete(favorite: Favorite)

    @Update
    fun update(favorite: Favorite)

    @Query("SELECT * FROM Favorite WHERE id = :id")
    fun deleteById(id: String) : Int

    @Query("SELECT * FROM Favorite WHERE id = :id and category = :category")
    fun getById(id: String, category: String): LiveData<List<Favorite>>

    @Query("SELECT * FROM Favorite WHERE category = :category")
    fun getByCategory(category: String): LiveData<List<Favorite>>

    @Query("SELECT * FROM Favorite")
    fun getAll(): List<Favorite>

    @Query("SELECT * FROM Favorite")
    fun getAllCursorFav(): Cursor

    @Query("SELECT * FROM Favorite WHERE id = :id")
    fun getCursorFavById(id: String): Cursor

}