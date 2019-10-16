package com.im.layarngaca21.provider

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.im.layarngaca21.database.AppDatabase
import com.im.layarngaca21.database.DatabaseContract.AUTHORITY
import com.im.layarngaca21.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.im.layarngaca21.database.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.im.layarngaca21.database.entity.Favorite

class MovieFavProvider : ContentProvider() {

    companion object {

        /*
        Integer digunakan sebagai identifier antara select all sama select by id
         */
        private const val MOVIE = 1
        private const val MOVIE_ID = 2
        private var db: AppDatabase? = null

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        /*
        Uri matcher untuk mempermudah identifier dengan menggunakan integer
        misal
        uri com.dicoding.picodiploma.mynotesapp dicocokan dengan integer 1
        uri com.dicoding.picodiploma.mynotesapp/# dicocokan dengan integer 2
         */
        init {
            // content://com.dicoding.picodiploma.mynotesapp/note
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE)

            // content://com.dicoding.picodiploma.mynotesapp/note/id
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", MOVIE_ID)
        }
    }

    override fun onCreate(): Boolean {
        db = AppDatabase.getAppDataBase(context as Context)

        return true
    }


    override fun query(uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?, s1: String?): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            MOVIE -> cursor = db?.favoriteDao()?.getAllCursorFav()
            MOVIE_ID -> cursor =  db?.favoriteDao()?.getCursorFavById(uri.lastPathSegment.toString())
            else -> cursor = null
        }

        return cursor
    }


    override fun getType(uri: Uri): String? {
        return null
    }


    override fun insert(uri: Uri, contentValues: ContentValues): Uri? {
        val added: Long = when (MOVIE) {
            sUriMatcher.match(uri) -> db?.favoriteDao()!!.insert(fromContentValues(contentValues))
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }


    override fun update(uri: Uri, contentValues: ContentValues, s: String?, strings: Array<String>?): Int {
        val updated: Int = when (MOVIE_ID) {
            sUriMatcher.match(uri) -> db?.favoriteDao()?.update(fromContentValues(contentValues)) as Int
            else -> 0
        }


        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (MOVIE_ID) {
            sUriMatcher.match(uri) -> db?.favoriteDao()!!.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }


        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    fun fromContentValues(values: ContentValues): Favorite {
        return Favorite(
            id = if (values.containsKey("id"))values.getAsString("id") else "",
            poster = if (values.containsKey("poster"))values.getAsString("poster") else "",
            synopsis = if (values.containsKey("synopsis"))values.getAsString("synopsis") else "",
            rate = if (values.containsKey("rate"))values.getAsString("rate") else "",
            title = if (values.containsKey("title"))values.getAsString("title") else "",
            category = if (values.containsKey("category"))values.getAsString("category") else "",
            date = if (values.containsKey("date"))values.getAsString("date") else "")
    }

}