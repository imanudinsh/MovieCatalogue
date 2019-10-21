package com.im.topmovie.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.im.layarngaca21"
    const val SCHEME = "content"

    class FavColumns : BaseColumns {

        companion object {
            const val TABLE_NAME = "Favorite"
            const val _ID = "_id"
            const val TITLE = "title"
            const val SYNOPSIS = "synopsis"
            const val RATE = "rate"
            const val POSTER = "poster"
            const val DATE = "date"
            const val CATEGORY = "category"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }

    }
}