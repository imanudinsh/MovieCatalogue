package com.im.layarngaca21.view.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.os.Bundle
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.im.layarngaca21.R
import com.im.layarngaca21.database.entity.Favorite
import com.im.layarngaca21.model.Movie
import com.im.layarngaca21.utils.CustomToast
import com.im.layarngaca21.utils.ViewMessages
import com.im.layarngaca21.utils.values.CategoryEnum
import com.im.layarngaca21.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_movie.*
import android.appwidget.AppWidgetManager
import android.content.Intent
import com.im.layarngaca21.view.widget.ImageBannerWidget


class MovieFragment : Fragment(), ViewMessages{


    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var adapter: MovieViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_movie, container, false)

        adapter = MovieViewAdapter(activity!!, favListener = {movie, ivHeart, isFavorite ->
            val fav = Favorite(id= movie.id, title = movie.title, date = movie.releaseDate, rate = movie.rate, synopsis = movie.synopsis, poster = movie.poster, category = CategoryEnum.MOVIE.value)
            if(isFavorite){
                moviesViewModel.deleteFavorite(fav)
                ivHeart.setImageDrawable(context?.getDrawable(R.drawable.ic_heart))
                ivHeart.imageTintList = context?.getColorStateList(R.color.grey)
                adapter.removeFavorite(fav)
            }else{
                moviesViewModel.insertFavorite(fav)
                adapter.addFavorite(fav)
                context?.let {
                    val ivAnimation = AnimatedVectorDrawableCompat.create(it, R.drawable.ic_heart_anim)
                    ivHeart.setImageDrawable(ivAnimation)
                    ivAnimation?.start()
                }

            }
        })
        adapter.notifyDataSetChanged()

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerCardView()
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel::class.java)
        moviesViewModel.onViewAttached()
        moviesViewModel.getAllFavorites().observe(this, getFavorite)
        moviesViewModel.getMovies().observe(this, getMovie)
        moviesViewModel.messagesEvent.setEventReceiver(this, this)
        moviesViewModel.setMovies()
        progressBar.visibility = View.VISIBLE

    }

    private val getFavorite = object : Observer<List<Favorite>?> {
        override fun onChanged(listFav: List<Favorite>?) {
            if (listFav != null) {
                adapter.setFavorites(listFav)
            }

            val intent = Intent(activity, ImageBannerWidget::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(ComponentName(context,ImageBannerWidget::class.java))
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            context?.sendBroadcast(intent)
            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.stack_view)
        }
    }
    private val getMovie = object : Observer<List<Movie>?> {
        override fun onChanged(listMovie: List<Movie>?) {
            if (listMovie != null) {
                adapter.setData(listMovie)
                progressBar.visibility = View.GONE
                Log.d("MovieFragment","$listMovie")
            }
        }
    }

    private fun showRecyclerCardView() {
        rv_category.layoutManager = LinearLayoutManager(context)
        rv_category.adapter = adapter
    }

    override fun showMessage(message: Int, category: String) {
        context?.let { CustomToast().show(it, resources.getString(message), category) }
        progressBar.visibility = View.GONE
        view_no_data.visibility = if(adapter.itemCount>0) View.GONE else View.VISIBLE
    }


}
