package com.im.layarngaca21.view.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import com.im.layarngaca21.R
import kotlinx.android.synthetic.main.activity_search.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.view.KeyEvent
import android.view.View
import com.im.layarngaca21.database.entity.Favorite
import com.im.layarngaca21.model.Movie
import com.im.layarngaca21.model.TV
import com.im.layarngaca21.utils.CustomToast
import com.im.layarngaca21.utils.ViewMessages
import com.im.layarngaca21.utils.values.CategoryEnum
import com.im.layarngaca21.view.main.MovieViewAdapter
import com.im.layarngaca21.view.main.TVShowViewAdapter
import com.im.layarngaca21.viewmodel.SearchViewModel
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import com.im.layarngaca21.view.widget.ImageBannerWidget


class SearchActivity : AppCompatActivity(), ViewMessages {


    private lateinit var searchViewModel: SearchViewModel
    private lateinit var movieAdapter: MovieViewAdapter
    private lateinit var tvAdapter: TVShowViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        main_content.visibility = View.GONE
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        searchViewModel.onViewAttached()
        searchViewModel.getMovieFavorites().observe(this, getMovieFavorite)
        searchViewModel.getTvFavorites().observe(this, getTvFavorite)
        searchViewModel.getMovies().observe(this, getMovie)
        searchViewModel.getTVShows().observe(this, getTv)
        searchViewModel.messagesEvent.setEventReceiver(this, this)

        movieAdapter = MovieViewAdapter(this, favListener = {movie, ivHeart, isFavorite ->
            val fav = Favorite(id= movie.id, title = movie.title, date = movie.releaseDate, rate = movie.rate, synopsis = movie.synopsis, poster = movie.poster, category = CategoryEnum.MOVIE.value)
            if(isFavorite){
                searchViewModel.deleteMovieFavorite(fav)
                ivHeart.setImageDrawable(this.getDrawable(R.drawable.ic_heart))
                ivHeart.imageTintList = this.getColorStateList(R.color.grey)
                movieAdapter.removeFavorite(fav)
            }else{
                searchViewModel.insertMovieFavorite(fav)
                movieAdapter.addFavorite(fav)
                val ivAnimation = AnimatedVectorDrawableCompat.create(this, R.drawable.ic_heart_anim)
                ivHeart.setImageDrawable(ivAnimation)
                ivAnimation?.start()

            }
        })
        tvAdapter = TVShowViewAdapter(this, favListener = {movie, ivHeart, isFavorite ->
            val fav = Favorite(id= movie.id, title = movie.name, date = movie.firsAirDate, rate = movie.rate, synopsis = movie.synopsis, poster = movie.poster, category = CategoryEnum.TV.value)
            if(isFavorite){
                searchViewModel.deleteTvFavorite(fav)
                ivHeart.setImageDrawable(this.getDrawable(R.drawable.ic_heart))
                ivHeart.imageTintList = this.getColorStateList(R.color.grey)
                tvAdapter.removeFavorite(fav)
            }else{
                searchViewModel.insertTvFavorite(fav)
                tvAdapter.addFavorite(fav)
                val ivAnimation = AnimatedVectorDrawableCompat.create(this, R.drawable.ic_heart_anim)
                ivHeart.setImageDrawable(ivAnimation)
                ivAnimation?.start()

            }
        })
        movieAdapter.notifyDataSetChanged()
        tvAdapter.notifyDataSetChanged()


        showRecyclerCardView()

        et_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(et_search.text.toString())
                    return true
                }
                return false
            }
        })

        btn_search.setOnClickListener {
            performSearch(et_search.text.toString())
        }

        btn_expand_movie.setOnClickListener {
            rv_movie.visibility = if(rv_movie.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            iv_arrow_movie.rotation = if(rv_movie.visibility == View.VISIBLE) 0f  else 180f
        }

        btn_expand_tv.setOnClickListener {
            rv_tv.visibility = if(rv_tv.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            iv_arrow_tv.rotation = if(rv_tv.visibility == View.VISIBLE) 0f  else 180f
        }

    }



    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home ->{
            onBackPressed()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private val getMovieFavorite = object : Observer<List<Favorite>?> {
        override fun onChanged(listFav: List<Favorite>?) {
            if (listFav != null) {
                movieAdapter.setFavorites(listFav)
            }

        }
    }
    private val getTvFavorite = object : Observer<List<Favorite>?> {
        override fun onChanged(listFav: List<Favorite>?) {
            if (listFav != null) {
                tvAdapter.setFavorites(listFav)
            }
        }
    }
    private val getMovie = object : Observer<List<Movie>?> {
        override fun onChanged(listMovie: List<Movie>?) {
            if (listMovie != null) {
                movieAdapter.setData(listMovie)
                progressBar.visibility = View.GONE
                Log.d("MovieFragment","$listMovie")
            }
            tv_text_result_movie.text = String.format("%d movies found", movieAdapter.itemCount)
            main_content.visibility = if(movieAdapter.itemCount==0 && tvAdapter.itemCount==0) View.GONE else View.VISIBLE
            view_no_data.visibility = if(movieAdapter.itemCount==0 && tvAdapter.itemCount==0) View.VISIBLE else View.GONE
            updateWidget()
        }
    }

    private val getTv = object : Observer<List<TV>?> {
        override fun onChanged(listTv: List<TV>?) {
            if (listTv != null) {
                tvAdapter.setData(listTv)
                progressBar.visibility = View.GONE
                Log.d("TvFragment","$listTv")
            }
            tv_text_result_tv.text = String.format("%d TV shows found", tvAdapter.itemCount)
            main_content.visibility = if(movieAdapter.itemCount==0 && tvAdapter.itemCount==0) View.GONE else View.VISIBLE
            view_no_data.visibility = if(movieAdapter.itemCount==0 && tvAdapter.itemCount==0) View.VISIBLE else View.GONE
            updateWidget()
        }
    }

    fun performSearch(keyWord: String){
        hideKeyboard(this)
        searchViewModel.searchMovies(keyWord)
        searchViewModel.searchTVShows(keyWord)
        progressBar.visibility = View.VISIBLE
    }

    private fun showRecyclerCardView() {
        rv_movie.isNestedScrollingEnabled = false
        rv_movie.layoutManager = LinearLayoutManager(this)
        rv_movie.adapter = movieAdapter
        rv_tv.isNestedScrollingEnabled = false
        rv_tv.layoutManager = LinearLayoutManager(this)
        rv_tv.adapter = tvAdapter
    }

    override fun showMessage(message: Int, category: String) {
        CustomToast().show(this, resources.getString(message), category) 
        progressBar.visibility = View.GONE
        view_no_data.visibility = if(movieAdapter.itemCount>0 && tvAdapter.itemCount>0) View.GONE else View.VISIBLE
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun updateWidget(){
        val intent = Intent(this@SearchActivity, ImageBannerWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val appWidgetManager = AppWidgetManager.getInstance(this@SearchActivity)
        val ids = appWidgetManager.getAppWidgetIds(
            ComponentName(this@SearchActivity,
                ImageBannerWidget::class.java)
        )
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
        appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.stack_view)
    }
}
