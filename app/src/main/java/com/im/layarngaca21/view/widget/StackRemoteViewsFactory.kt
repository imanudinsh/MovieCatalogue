package com.im.layarngaca21.view.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.im.layarngaca21.R
import com.im.layarngaca21.database.entity.Favorite
import com.bumptech.glide.Glide
import android.graphics.Bitmap
import android.os.Binder
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.request.target.Target
import com.im.layarngaca21.database.AppDatabase
import com.im.layarngaca21.database.DatabaseContract.FavColumns.Companion.CATEGORY
import com.im.layarngaca21.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.im.layarngaca21.database.DatabaseContract.FavColumns.Companion.DATE
import com.im.layarngaca21.database.DatabaseContract.FavColumns.Companion.POSTER
import com.im.layarngaca21.database.DatabaseContract.FavColumns.Companion.RATE
import com.im.layarngaca21.database.DatabaseContract.FavColumns.Companion.SYNOPSIS
import com.im.layarngaca21.database.DatabaseContract.FavColumns.Companion.TITLE
import com.im.layarngaca21.model.Movie
import java.util.concurrent.ExecutionException


internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {


    private var listFavorites: MutableList<Favorite> = mutableListOf()
    lateinit var bmp:Bitmap

    override fun onCreate() {

    }

    override fun onDataSetChanged() {

        val identityToken = Binder.clearCallingIdentity()
        val cursor = mContext.contentResolver.query(CONTENT_URI, null, null, null, null) as Cursor
        mapCursorToList(cursor)
        Binder.restoreCallingIdentity(identityToken)

    }



    override fun onDestroy() {
    }

    override fun getCount(): Int{
        return if(listFavorites==null) 0 else listFavorites!!.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        try {
            bmp = Glide.with(mContext)
                .asBitmap()
                .load("https://image.tmdb.org/t/p/w185${listFavorites.get(position).poster}")
                .placeholder(R.drawable.img_placeholder)
                .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get()

        } catch (e: InterruptedException) {
            Log.d("Widget Load Error", "error")
        } catch (e: ExecutionException) {
            Log.d("Widget Load Error", "error")
        }

        rv.setImageViewBitmap(R.id.imageView, bmp)
        rv.setTextViewText(R.id.textView, listFavorites.get(position).title)



        val extras = Bundle()
        extras.putInt(ImageBannerWidget.EXTRA_ITEM, position)
        extras.putString("id", listFavorites.get(position).id)
        extras.putString(TITLE, listFavorites.get(position).title)
        extras.putString(RATE, listFavorites.get(position).rate)
        extras.putString(DATE, listFavorites.get(position).date)
        extras.putString(SYNOPSIS, listFavorites.get(position).synopsis)
        extras.putString(POSTER, listFavorites.get(position).poster)
        extras.putString(CATEGORY, listFavorites.get(position).category)

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    fun mapCursorToList(moviesCursor: Cursor?) {

        if(moviesCursor!=null) {
            listFavorites.clear()
            while (moviesCursor.moveToNext()) {
                val id = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow("id"))
                val title = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(TITLE))
                val synopsis = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(SYNOPSIS))
                val date = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DATE))
                val poster = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(POSTER))
                val rate = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(RATE))
                val category = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(CATEGORY))
                listFavorites.add(
                    Favorite(
                        id = id,
                        title = title,
                        synopsis = synopsis,
                        date = date,
                        poster = poster,
                        rate = rate,
                        category = category
                    )
                )
            }
        }

    }

}