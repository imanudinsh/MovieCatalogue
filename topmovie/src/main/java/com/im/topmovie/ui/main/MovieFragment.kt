package com.im.topmovie.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.database.Cursor
import android.os.Binder
import android.util.Log
import com.im.topmovie.R
import com.im.topmovie.database.DatabaseContract
import com.im.topmovie.model.Movie
import com.im.topmovie.utils.values.CategoryEnum
import kotlinx.android.synthetic.main.fragment_movie.*


class MovieFragment : androidx.fragment.app.Fragment(){


    private lateinit var adapter: MovieViewAdapter
    private var listMovie: MutableList<Movie> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_movie, container, false)

        adapter = MovieViewAdapter(activity!!)
        adapter.notifyDataSetChanged()

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            listMovie.clear()
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
                if(category == CategoryEnum.MOVIE.value)
                    listMovie.add(
                        Movie(
                            id = id,
                            title = title,
                            synopsis = synopsis,
                            releaseDate = date,
                            poster = poster,
                            rate = rate
                        )
                    )
            }
            Log.d("MovieFragment", "Data $listMovie")
            adapter.setData(listMovie)
            adapter.notifyDataSetChanged()
        }


        view_no_data.visibility = if(listMovie.size>0) View.GONE else View.VISIBLE

    }



}
