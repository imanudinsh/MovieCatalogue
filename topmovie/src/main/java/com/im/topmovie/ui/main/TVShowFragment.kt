package com.im.topmovie.ui.main

import android.database.Cursor
import android.os.Binder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.im.topmovie.R
import com.im.topmovie.database.DatabaseContract
import com.im.topmovie.model.TV
import com.im.topmovie.utils.values.CategoryEnum
import kotlinx.android.synthetic.main.fragment_tv_show.*


class TVShowFragment : androidx.fragment.app.Fragment() {

    private lateinit var adapter: TVShowViewAdapter
    private var listTV: MutableList<TV> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_tv_show, container, false)

        adapter = TVShowViewAdapter(activity!!)
        adapter.notifyDataSetChanged()

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerCardView()

        showRecyclerCardView()
        val identityToken = Binder.clearCallingIdentity()
        val cursor = context?.contentResolver?.query(DatabaseContract.FavColumns.CONTENT_URI, null, null, null, null) as Cursor
        mapCursorToList(cursor)

        Binder.restoreCallingIdentity(identityToken)

    }

    private fun showRecyclerCardView() {
        rv_category.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rv_category.adapter = adapter
    }


    fun mapCursorToList(moviesCursor: Cursor?) {

        if(moviesCursor!=null) {
            listTV.clear()
            while (moviesCursor.moveToNext()) {
                val id = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow("id"))
                val title = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavColumns.TITLE
                ))
                val synopsis = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavColumns.SYNOPSIS
                ))
                val date = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavColumns.DATE
                ))
                val poster = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavColumns.POSTER
                ))
                val rate = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavColumns.RATE
                ))
                val category = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavColumns.CATEGORY
                ))
                if(category == CategoryEnum.TV.value)
                    listTV.add(
                        TV(
                            id = id,
                            name = title,
                            synopsis = synopsis,
                            firsAirDate = date,
                            poster = poster,
                            rate = rate
                        )
                    )
            }
            Log.d("MovieFragment", "Data $listTV")
            adapter.setData(listTV)
            adapter.notifyDataSetChanged()
        }

        view_no_data.visibility = if(listTV.size>0) View.GONE else View.VISIBLE
    }


}
