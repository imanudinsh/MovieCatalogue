package com.im.layarngaca21.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Log
import com.google.gson.Gson
import com.im.layarngaca21.BuildConfig
import com.im.layarngaca21.R
import com.im.layarngaca21.database.AppDatabase
import com.im.layarngaca21.database.entity.Favorite
import com.im.layarngaca21.database.repository.FavoriteRepository
import com.im.layarngaca21.utils.values.CategoryEnum


class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var db: AppDatabase? = null
    private lateinit var repoMovie: FavoriteRepository
    private lateinit var repoTV: FavoriteRepository
    private lateinit var repo: FavoriteRepository
    private lateinit var movieFavorite: LiveData<List<Favorite>>
    private lateinit var tvFavorite: LiveData<List<Favorite>>
    private val app = application


    internal fun onViewAttached(){
        db = AppDatabase.getAppDataBase(app)
        repoMovie = FavoriteRepository(db!!.favoriteDao(), CategoryEnum.MOVIE.value)
        repoTV = FavoriteRepository(db!!.favoriteDao(), CategoryEnum.TV.value)
        repo = FavoriteRepository(db!!.favoriteDao(), CategoryEnum.TV.value)
        movieFavorite = repoMovie.getAllFavorites()
        tvFavorite = repoTV.getAllFavorites()
    }

    internal fun insertMovieFavorite(fav: Favorite) {
        repoMovie.insert(fav)
    }

    internal fun insertTvFavorite(fav: Favorite) {
        repoTV.insert(fav)
    }

    internal fun deleteMovieFavorite(fav: Favorite) {
        repoMovie.delete(fav)
    }

    internal fun deleteTvFavorite(fav: Favorite) {
        repoTV.delete(fav)
    }

    internal fun getMovieFavorites(): LiveData<List<Favorite>> {
        return movieFavorite
    }

    internal fun getTvFavorites(): LiveData<List<Favorite>> {
        return tvFavorite
    }

}
